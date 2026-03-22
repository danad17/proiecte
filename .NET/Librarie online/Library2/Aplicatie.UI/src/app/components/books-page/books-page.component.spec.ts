import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SuggestedBookComponent } from '../books/suggested-books/suggested-books';

describe('BooksListComponent', () => {
  let component: SuggestedBookComponent;
  let fixture: ComponentFixture<SuggestedBookComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SuggestedBookComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SuggestedBookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
