import { NULL_EXPR } from '@angular/compiler/src/output/output_ast';
import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, take } from 'rxjs';
import { ListSubscriptionResult } from 'src/app/data/ListSubscriptionResult';
import { SubscribeTopicRequest } from 'src/app/data/SubscribeTopicRequest';
import { SubscribeTopicResult } from 'src/app/data/SubscribeTopicResult';
import { Subscription } from 'src/app/data/Subscription';
import { UnsubscribeTopicRequest } from 'src/app/data/UnsubscribeTopicRequest';
import { UnsubscribeTopicResult } from 'src/app/data/UnsubscribeTopicResult';
import { ConfigService } from 'src/app/services/config.service';
import { SnsSubscriptionService } from 'src/app/services/sns-subscription.service';

@Component({
  selector: 'app-sns-subscriptions',
  templateUrl: './sns-subscriptions.component.html',
  styleUrls: ['./sns-subscriptions.component.scss']
})
export class SnsSubscriptionsComponent implements OnInit {
  freezeButtons = new BehaviorSubject(false);

  topicNameFilter = "jumbo-client-facade"
  regionFilter = "eu-west-1"
  protocolFilter = "EMAIL"
  endpointFilter = ""

  listSubscriptionResult: ListSubscriptionResult = new ListSubscriptionResult()
  subscribeTopicResult: SubscribeTopicResult | null = null
  unsubscribeTopicResult: UnsubscribeTopicResult | null = null

  subscribeTopicRequest = new SubscribeTopicRequest()
  unsubscribeTopicRequest = new UnsubscribeTopicRequest()
  displayedColumns: string[] = ['region', 'subscriptionArn', 'position', 'endpoint', 'protocol', 'unsubscribe'];

  messageCreate = "ㅤ"
  messageCreateError = false
  messageLoad = "ㅤ"
  messageLoadError = false
  messageDelete = "ㅤ"
  messageDeleteError = false


  constructor(public subscriptionService: SnsSubscriptionService, public configService: ConfigService) {
    configService.doTimeoutForLoading(true);
  }

  ngOnInit(): void {
    this.configService
      .userConfiguration
      .pipe(take(1))
      .subscribe(rsp => {
        this.endpointFilter = rsp.defaultEmail
        this.subscribeTopicRequest.endpoint = rsp.defaultEmail
      })
  }

  public load(topicName: string, region: string, protocol: string, endpoint: string) {
    this.messageLoad = "ㅤ"
    this.messageLoadError = false
    this.freezeButtons.next(true)
    this.subscriptionService
      .load(topicName, region, protocol, endpoint)
      .subscribe({
        next: rsp => {
          this.listSubscriptionResult = rsp
          this.messageLoad = "Loading Successful!"
        }, error: err => {
          this.messageLoad = err?.error?.message
          this.messageLoadError = true
        },
        complete: () => {

        }
      }).add(() => {
        this.freezeButtons.next(false)
      })
  }

  public subscribe(request: SubscribeTopicRequest) {
    this.messageCreate = "ㅤ"
    this.messageCreateError = false
    this.freezeButtons.next(true)
    this.subscriptionService
      .subscribe(request)
      .subscribe({
        next: rsp => {
          this.subscribeTopicResult = rsp
          this.messageCreate = "Subscription was created (or already existed)!"
        }, error: err => {
          this.messageCreate = err?.error?.message
          this.messageCreateError = true
        }, complete: () => {
        }
      }).add(() => {
        this.freezeButtons.next(false)
      })
  }

  public unsubscribe(request: UnsubscribeTopicRequest) {
    this.messageDelete = "ㅤ"
    this.messageDeleteError = false
    this.freezeButtons.next(true)

    this.subscriptionService
      .unsubscribe(request)
      .subscribe({
        next: rsp => {
          this.unsubscribeTopicResult = rsp
          this.messageDelete = "Unsubscribing was successful!"
        }, error: err => {
          this.messageDelete = err?.error?.message
          this.messageDeleteError = true
        }, complete: () => {
        }
      }).add(() => {
        this.load(this.topicNameFilter, this.regionFilter, this.protocolFilter, this.endpointFilter)
        this.freezeButtons.next(false)
      })
  }

}
