import { NULL_EXPR } from '@angular/compiler/src/output/output_ast';
import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ListSubscriptionResult } from 'src/app/data/ListSubscriptionResult';
import { SubscribeTopicRequest } from 'src/app/data/SubscribeTopicRequest';
import { SubscribeTopicResult } from 'src/app/data/SubscribeTopicResult';
import { Subscription } from 'src/app/data/Subscription';
import { UnsubscribeTopicRequest } from 'src/app/data/UnsubscribeTopicRequest';
import { UnsubscribeTopicResult } from 'src/app/data/UnsubscribeTopicResult';
import { SnsSubscriptionService } from 'src/app/services/sns-subscription.service';

@Component({
  selector: 'app-sns-subscriptions',
  templateUrl: './sns-subscriptions.component.html',
  styleUrls: ['./sns-subscriptions.component.scss']
})
export class SnsSubscriptionsComponent implements OnInit {
  freezeButtons = new BehaviorSubject(false);

  topicNameFilter = ""
  regionFilter = "eu-west-1"
  protocolFilter = "EMAIL"
  endpointFilter = ""

  listSubscriptionResult: ListSubscriptionResult | null = null
  subscribeTopicResult: SubscribeTopicResult | null = null
  unsubscribeTopicResult: UnsubscribeTopicResult | null = null

  subscribeTopicRequest = new SubscribeTopicRequest()
  unsubscribeTopicRequest = new UnsubscribeTopicRequest()

  constructor(public subscriptionService: SnsSubscriptionService) {

  }

  ngOnInit(): void {
  }

  public load(topicName: string, region: string, protocol: string, endpoint: string) {
    this.freezeButtons.next(true)
    this.subscriptionService
      .load(topicName, region, protocol, endpoint)
      .subscribe({
        next: rsp => {
          this.listSubscriptionResult = rsp
        }, error: err => {

        },
        complete: () => {

        }
      }).add(() => {
        this.freezeButtons.next(false)
      })
  }

  public subscribe(request: SubscribeTopicRequest) {
    this.freezeButtons.next(true)
    this.subscriptionService
      .subscribe(request)
      .subscribe({
        next: rsp => {
          this.subscribeTopicResult = rsp
        }, error: err => {
        }, complete: () => {
        }
      }).add(() => {
        this.freezeButtons.next(false)
      })
  }

  public unsubscribe(request: UnsubscribeTopicRequest) {
    this.freezeButtons.next(true)
    this.subscriptionService
      .unsubscribe(request)
      .subscribe({
        next: rsp => {
          this.unsubscribeTopicResult = rsp
        }, error: err => {
        }, complete: () => {
        }
      }).add(() => {
        this.freezeButtons.next(false)
      })
  }

}
