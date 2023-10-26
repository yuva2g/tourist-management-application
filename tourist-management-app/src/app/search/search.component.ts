import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TouristService } from '../tourist.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit {
  selectedCriteria = 'branchId';
  searchValue = '';
  placeholderText = 'Enter a branch ID...';
  searchResults: any[] = []; // Update to match your data structure
  isSearchValueEmpty: boolean = false;
  isError: boolean = false;
  errorMessage: string = '';

  constructor(private router: Router, private touristService: TouristService) {}

  ngOnInit(): void {}

  updatePlaceholder() {
  if (this.selectedCriteria === 'branchName') {
      this.placeholderText = 'Enter a branch name...';
    } else if (this.selectedCriteria === 'branchId') {
      this.placeholderText = 'Enter a branch ID...';
    } else {
      this.placeholderText = 'Enter your search criteria...';
    }

    this.searchValue = this.selectedCriteria === 'places' ? 'ANDAMAN' : '';
    console.log(this.searchValue);
  }

  search() {
    if (!this.searchValue) {
      this.isSearchValueEmpty = true;
      setTimeout(() => {
        this.isSearchValueEmpty = false;
      }, 5000);
      return;
    } else{
      this.isSearchValueEmpty = false;
    }
    this.touristService
      .search(this.selectedCriteria, this.searchValue)
      .subscribe((data: any) => {
        this.isError = false;
        this.searchResults = data;
      }, (err: { error: string; }) => {
        this.isError = true;
        setTimeout(() => {
          this.isError = false;
        }, 5000);
        this.errorMessage = err.error;
      });
  }
}
