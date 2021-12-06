import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { UserConfiguration } from '../data/UserConfiguration';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  public userConfiguration = new BehaviorSubject<UserConfiguration | null>(null);
  constructor(private http: HttpClient) { }

  load() {
    this.http.get<UserConfiguration>(`api/xxx`)
      .subscribe(uc => {
        this.userConfiguration.next(uc);
      });
  }
}
