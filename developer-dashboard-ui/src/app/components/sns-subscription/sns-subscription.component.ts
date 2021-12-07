import { Component, Input, OnInit } from '@angular/core';
import { Subscription } from 'src/app/data/Subscription';
import { UnsubscribeTopicRequest } from 'src/app/data/UnsubscribeTopicRequest';
import { SnsSubscriptionService } from 'src/app/services/sns-subscription.service';

@Component({
  selector: 'app-sns-subscription',
  templateUrl: './sns-subscription.component.html',
  styleUrls: ['./sns-subscription.component.scss']
})
export class SnsSubscriptionComponent implements OnInit {

  @Input() subscription = new Subscription();

  constructor(private snsSubscriptionService: SnsSubscriptionService) {

  }

  ngOnInit(): void {
  }

  unsubscribe() {
    const request = new UnsubscribeTopicRequest();
    request.region = this.subscription.region
    request.subscriptionArn = this.subscription.subscriptionArn
    this.snsSubscriptionService.unsubscribe(request);
  }

}
