import { Component, OnInit } from '@angular/core';
import { ConfigService } from 'src/app/services/config.service';

@Component({
  selector: 'app-c2',
  templateUrl: './c2.component.html',
  styleUrls: ['./c2.component.scss']
})
export class C2Component implements OnInit {

  configString = ""

  constructor(public configService: ConfigService) {
    configService.userConfiguration.subscribe(
      {
        next: rsp => {
          this.configString = JSON.stringify(rsp, null, 4);
        }
      }
    );
    configService.forceLoading();
  }

  ngOnInit(): void {
  }


}
