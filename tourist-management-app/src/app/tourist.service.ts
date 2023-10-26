import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TouristService {
  constructor(private http: HttpClient) {}

  search(selectedCriteria: string, searchValue: string): any {
    return this.http.get(`http://localhost:8083/tourism/api/v1/branch/admin/${selectedCriteria}/${searchValue}`);

  //   return of([
  //     {
  //         id: 1,
  //         branchName: "Cognizant SRC",
  //         website: "www.example.com",
  //         contact: "1234567890",
  //         email: "example@example.com",
  //         touristPlaces: [{
  //           name: "ANDAMAN",
  //           tariff: 55000.0
  //         },
  //         {
  //           name: "DUBAI",
  //           tariff: 65000.0
  //         }]
  //     },
  //     {
  //       id: 2,
  //       branchName: "Cognizant SRC",
  //       website: "www.example.com",
  //       contact: "1234567890",
  //       email: "example@example.com",
  //       touristPlaces: [{
  //         name: "ANDAMAN",
  //         tariff: 55000.0
  //       },
  //       {
  //         name: "DUBAI",
  //         tariff: 65000.0
  //       }]
  //   }
  // ])
  }
}