import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AuthCheckResult } from 'src/app/data/AuthCheckResult';
import { CalendarEvent } from 'src/app/data/CalendarEvent';
import { CalendarEventListResponse } from 'src/app/data/CalendarEventListResponse';
import { UpdateEventRequest } from 'src/app/data/UpdateEventRequest';
import { PortService } from 'src/app/services/port.service';

@Component({
  selector: 'app-ro-calendar',
  templateUrl: './ro-calendar.component.html',
  styleUrls: ['./ro-calendar.component.scss']
})
export class RoCalendarComponent implements OnInit {

  events: CalendarEvent[] = []
  working = false
  constructor(private httpClient: HttpClient, private portService: PortService) { }

  ngOnInit(): void {
    this.portService
      .sendPortToServer()
      .add(() => {
        this.checkAuth(() => {
          this.loadEvents()
        })
      })
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

  checkAuth(authOk: Function) {
    this.httpClient.get<AuthCheckResult>(`api/ro-calender:check-auth`)
      .subscribe({
        next: rsp => {
          if (rsp.authCheckState === "OK") {
            console.log("auth check was ok")
            authOk()
          } else if (rsp.authCheckState === "AUTH_REQUIRED") {
            console.log("auth check was not ok")
            window.location.href = rsp.authUrl
          }
        }
      })
  }

  unreserve(id: string) {
    this.working = true
    this.portService.sendPortToServer()
    const updateEventRequest = new UpdateEventRequest()
    updateEventRequest.description = "Empty slot"
    this.updateEvent(id, updateEventRequest)
  }
  reserve(id: string) {

    this.working = true
    this.portService.sendPortToServer()
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
