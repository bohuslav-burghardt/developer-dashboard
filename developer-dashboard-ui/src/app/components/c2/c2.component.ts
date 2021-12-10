import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { take } from 'rxjs';
import { ConfigService } from 'src/app/services/config.service';

@Component({
  selector: 'app-c2',
  templateUrl: './c2.component.html',
  styleUrls: ['./c2.component.scss']
})
export class C2Component implements OnInit {

  configString = ""
  parsingError = false
  working = false

  constructor(public configService: ConfigService, private httpClient: HttpClient) {
    this.loadConfig()
  }

  loadConfig() {
    this.working = true
    this.configService.userConfiguration
      .pipe(take(1))
      .subscribe(
        {
          next: rsp => {
            console.log("HEELLL")
            this.configString = JSON.stringify(rsp, null, 4);
          }
        }
      ).add(() => {
        this.working = false
      });
    this.configService.forceLoading();
  }

  ngOnInit(): void {
  }
  save() {
    this.working = true
    this.parsingError = false
    let parsedUserCfg;
    try {
      parsedUserCfg = JSON.parse(this.configString);
    } catch (e) {
      this.parsingError = true
      this.working = false
      return
    }
    this.httpClient.post<any>(`api/configuration/update`, parsedUserCfg)
      .subscribe({
        next: rsp => {

        }
      }).add(() => {
        this.working = false
      })
  }


}
