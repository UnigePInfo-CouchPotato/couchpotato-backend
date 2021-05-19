import { TestBed } from '@angular/core/testing';

import { ImdbQueryService } from './imdb-query.service';

describe('ImdbQueryService', () => {
  let service: ImdbQueryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImdbQueryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
