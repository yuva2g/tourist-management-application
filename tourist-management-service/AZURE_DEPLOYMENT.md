
az login

# tag="yuva-fse-tag"
resourceGroup="rg-azuser776_mml.local-ltKqy"
location="Central India"
# Create a resource group.
echo "Creating $resourceGroup in "$location"..."
# az group create --name $resourceGroup --location "$location" --tag $tag

server="yuva-fse-sql-server"
database="yuvafsedb"
login="azureuser"
password="Pa$$w0rD-Yuva-F$E"
# Specify appropriate IP address values for your environment
# to limit access to the SQL Database server
startIp=0.0.0.0
endIp=0.0.0.0
# Create a SQL API Cosmos DB account with session consistency and multi-master enabled
echo "Creating $server in $location..."
az sql server create --name $server --resource-group $resourceGroup --location "$location" --admin-user $login --admin-password $password
echo "Configuring firewall..."
az sql server firewall-rule create --resource-group $resourceGroup --server $server -n AllowYourIp --start-ip-address $startIp --end-ip-address $endIp
echo "Creating $database on $server..."
az sql db create --resource-group $resourceGroup --server $server --name $database --sample-name AdventureWorksLT --edition GeneralPurpose --family Gen5 --capacity 2 --zone-redundant true 

cat << EOF > create_user.sql
USE yuvafsedb;
GO
CREATE USER yuvafseuser WITH PASSWORD='yuvaFse_pa$$w0rd';
GO
GRANT CONTROL ON DATABASE::yuvafsedb TO yuvafseuser;
GO
EOF

sqlcmd -S yuva-fse-sql-server.database.windows.net,1433 -d yuvafsedb -U azureuser -P Pa$$w0rD-Yuva-F$E -i create_user.sql


account="yuva-fse-cosmos-account" #needs to be lower case
database="yuva-fse-mongo-cosmos-db"
serverVersion="4.0" #3.2, 3.6, 4.0
collection="touristCompanies"
# Create a MongoDB API Cosmos DB account
echo "Creating $account"
az cosmosdb create --name $account --resource-group $resourceGroup --kind MongoDB --server-version $serverVersion --default-consistency-level Eventual --enable-automatic-failover false --locations regionName="$location" failoverPriority=0 isZoneRedundant=False

# Create a MongoDB API database
echo "Creating $database"
az cosmosdb mongodb database create --account-name $account --resource-group $resourceGroup --name $database

# Define the index policy for the collection, with _id, wildcard, compound, unique and TTL
printf '
[
    {
    "key": {"keys": ["_id"]}
    },
    {
    "key": {"keys": ["$**"]}
    },
    {
    "key": {"keys": ["_ts"]},
    "options": {"expireAfterSeconds": 2629746}
    }
]' > idxpolicy-$randomIdentifier.json

# Create a MongoDB API collection
echo "Creating $collection"
az cosmosdb mongodb collection create --account-name $account --resource-group $resourceGroup --database-name $database --name $collection --shard "user_id" --throughput 400 --idx @idxpolicy-$randomIdentifier.json

# Clean up temporary index policy file
rm -f "idxpolicy-$randomIdentifier.json"

serviceBusNamespace="yuva-fse-service-bus-namespace"
serviceBusQueue="yuva-fse-service-bus-queue"
# Create a Azure Service Bus - Queue
az servicebus namespace create --resource-group $resourceGroup --name $serviceBusNamespace --location "$location" --sku Basic
az servicebus queue create --resource-group $resourceGroup --namespace-name $serviceBusNamespace --name $serviceBusQueue
az servicebus namespace authorization-rule keys list --resource-group $resourceGroup --namespace-name $serviceBusNamespace --name RootManageSharedAccessKey --query primaryConnectionString --output tsv

# Create an App Service app with deployment from GitHub
# set -e # exit if error
# Variable block
touristmanagementappGitRepo=https://github.com/yuva2g/tourist-management-app.git
touristmanagementqueryappGitRepo=https://github.com/yuva2g/tourist-management-query-app.git 
appServicePlan="yuva-fse-service-plan"
touristManagementApp="yuva-tourist-management-app"
touristManagementQueryApp="yuva-tourist-management-query-app"

# Create an App Service plan in `FREE` tier.
echo "Creating $appServicePlan"
az appservice plan create --name $appServicePlan --resource-group $resourceGroup --sku FREE

# Create a web app.
echo "Creating $touristManagementApp"
az webapp create --name $touristManagementApp --resource-group $resourceGroup --plan $appServicePlan

# Deploy code from a public GitHub repository.
az webapp deployment source config --name $touristManagementApp --resource-group $resourceGroup --repo-url $touristmanagementappGitRepo --branch master --manual-integration

# Use curl to see the web app.
site="http://$touristManagementApp.azurewebsites.net"
echo $site
curl "$site" 

# Create a web app.
echo "Creating $touristManagementQueryApp"
az webapp create --name $touristManagementQueryApp --resource-group $resourceGroup --plan $appServicePlan

# Deploy code from a public GitHub repository.
az webapp deployment source config --name $touristManagementQueryApp --resource-group $resourceGroup --repo-url $touristmanagementqueryappGitRepo --branch master --manual-integration

# Use curl to see the web app.
site="http://$touristManagementQueryApp.azurewebsites.net"
echo $site
curl "$site" 

# Create a Azure Web App - Angular app
# Follow the instructions on the website: https://henriquesd.medium.com/deploying-an-angular-application-in-azure-9f89edfe2b9c