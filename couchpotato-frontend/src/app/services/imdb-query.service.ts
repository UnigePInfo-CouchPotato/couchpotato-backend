import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

/** Service used to query IMDB API's */
@Injectable({
  providedIn: 'root'
})
export class ImdbQueryService {
  /** Creates an instance of ImdbQueryService.
   * @param http The client used to process API requests
   */
  constructor(private http: HttpClient) { }
}
