import { Component, OnInit } from '@angular/core';
import { InfoAtCursorService } from './services/info-at-cursor.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'my-first-project';
  constructor(public infoAtCursorService: InfoAtCursorService) {

  }
  ngOnInit(): void {
    this.infoAtCursorService.textElement = document.getElementById("infoAtCursor")
  }
}
