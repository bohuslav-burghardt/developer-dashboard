import { HttpClient } from '@angular/common/http';
import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { UserConfiguration } from '../data/UserConfiguration';

@Injectable({
  providedIn: 'root'
})
export class ConfigService implements OnDestroy {
  public userConfiguration = new BehaviorSubject<UserConfiguration | null>(null);
  private destroyed = false
  constructor(private http: HttpClient) {
    console.log("Starting ConfigService")
    this.doTimeoutForLoading()
    this.doTimeoutForReloading()
  }
  ngOnDestroy(): void {
    this.destroyed = true
  }

  doTimeoutForLoading() {
    if (this.destroyed) {
      console.log("doTimeoutForLoading called but ConfigService was destroyed, doTimeout will do nothing")
      return
    }

    this.load()
      .subscribe({
        next: rsp => {
          console.log("Loaded config!")
          this.userConfiguration.next(rsp)
        },
        error: rsp => {

        }
      }).add(() => {
        setTimeout(() => {
          this.doTimeoutForLoading()
        }, 10000)
      })
  }

  doTimeoutForReloading() {
    if (this.destroyed) {
      console.log("doTimeoutForReloading called but ConfigService was destroyed, doTimeout will do nothing")
      return
    }

    this.reloadOnServer()
      .add(() => {
        setTimeout(() => {
          this.doTimeoutForReloading()
        }, 10000)
      })


  }

  reloadOnServer() {
    return this.http.post<any>(`api/configuration/reload`, null)
      .subscribe({
        next: rsp => {
          console.log("reloaded config...")
        }
      })
  }

  load() {
    console.log("Loading config...")
    return this.http.get<UserConfiguration>(`api/configuration`)
  }
}
