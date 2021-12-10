import { HttpClient } from '@angular/common/http';
import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { UserConfiguration } from '../data/UserConfiguration';

@Injectable({
  providedIn: 'root'
})
export class ConfigService implements OnDestroy {
  public userConfiguration = new Subject<UserConfiguration>()
  private destroyed = false
  constructor(private http: HttpClient) {
    console.log("Starting ConfigService")
    this.doTimeoutForLoading()
  }
  ngOnDestroy(): void {
    this.destroyed = true
  }

  doTimeoutForLoading() {
    if (this.destroyed) {
      console.log("doTimeoutForReloading called but ConfigService was destroyed, doTimeout will do nothing")
      return
    }

    this.load()
      .add(() => {
        setTimeout(() => {
          this.doTimeoutForLoading()
        }, 10000)
      })
  }

  load() {
    console.log("Loading config...")
    return this.http.get<UserConfiguration>(`api/configuration`)
      .subscribe({
        next: rsp => {
          this.userConfiguration.next(rsp)
        }
      })
  }
}
