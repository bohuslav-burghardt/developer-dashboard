import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PortService {

  constructor(private httpClient: HttpClient) { }

  sendPortToServer() {
    console.log(`sending port <${window.location.port}> to server...`)
    return this.httpClient.post(`api/configuration/port`, { port: window.location.port })
      .subscribe({
        next: rsp => {
          console.log(`sent port <${window.location.port}> to server.`)
        }
      })
  }
}
