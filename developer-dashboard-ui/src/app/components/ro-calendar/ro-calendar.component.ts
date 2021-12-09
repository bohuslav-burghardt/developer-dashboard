import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { CalendarEvent } from 'src/app/data/CalendarEvent';
import { CalendarEventListResponse } from 'src/app/data/CalendarEventListResponse';
import { UpdateEventRequest } from 'src/app/data/UpdateEventRequest';

@Component({
  selector: 'app-ro-calendar',
  templateUrl: './ro-calendar.component.html',
  styleUrls: ['./ro-calendar.component.scss']
})
export class RoCalendarComponent implements OnInit {

  events: CalendarEvent[] = []
  working = false
  constructor(private httpClient: HttpClient) { }

  ngOnInit(): void {
    this.loadEvents()
  }

  loadEvents() {
    console.log("loading events...");
    this.working = true
    this.events = [];

    this.httpClient.get<CalendarEventListResponse>(`api/ro-calendar/ro-events`)
      .subscribe({
        next: rsp => {
          this.events = rsp.events
          console.log("successfully loaded events.");
        }
      })
      .add(() => {
        this.working = false
      })
  }

  unreserve(id: string) {
    this.working = true
    const updateEventRequest = new UpdateEventRequest()
    updateEventRequest.description = "Empty slot"
    this.updateEvent(id, updateEventRequest)
  }
  reserve(id: string) {
    this.working = true
    const updateEventRequest = new UpdateEventRequest()
    updateEventRequest.description = prompt("Please enter description for calendar event", "Empty slot") ?? "Empty slot";
    this.updateEvent(id, updateEventRequest)
  }
  updateEvent(id: string, updateEventRequest: UpdateEventRequest) {
    this.httpClient.post<any>(`api/ro-calendar/ro-event/${id}:reserve`, updateEventRequest)
      .subscribe({
        next: rsp => {

        }
      }).add(() => {
        this.loadEvents()
      })
  }
}
