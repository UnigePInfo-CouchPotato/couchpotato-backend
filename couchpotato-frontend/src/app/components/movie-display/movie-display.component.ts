import { Component } from '@angular/core';
import { ImdbQueryService } from 'src/app/services/imdb-query.service';

/** Displays a movie element */
@Component({
  selector: 'app-movie-display',
  templateUrl: './movie-display.component.html',
  styleUrls: ['./movie-display.component.scss']
})
export class MovieDisplayComponent {
  /** Creates an instance of MovieDisplayComponent. */
  constructor(private imbdQuery: ImdbQueryService) { }
}
