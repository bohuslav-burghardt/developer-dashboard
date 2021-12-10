import { NULL_EXPR } from '@angular/compiler/src/output/output_ast';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, take, takeWhile } from 'rxjs';
import { ListSubscriptionResult } from 'src/app/data/ListSubscriptionResult';
import { SubscribeTopicRequest } from 'src/app/data/SubscribeTopicRequest';
import { SubscribeTopicResult } from 'src/app/data/SubscribeTopicResult';
import { Subscription } from 'src/app/data/Subscription';
import { UnsubscribeTopicRequest } from 'src/app/data/UnsubscribeTopicRequest';
import { UnsubscribeTopicResult } from 'src/app/data/UnsubscribeTopicResult';
import { UserConfiguration } from 'src/app/data/UserConfiguration';
import { ConfigService } from 'src/app/services/config.service';
import { InfoAtCursorService } from 'src/app/services/info-at-cursor.service';
import { SnsSubscriptionService } from 'src/app/services/sns-subscription.service';

@Component({
  selector: 'app-sns-subscriptions',
  templateUrl: './sns-subscriptions.component.html',
  styleUrls: ['./sns-subscriptions.component.scss']
})
export class SnsSubscriptionsComponent implements OnInit, OnDestroy {
  destroyed = false
  freezeButtons = new BehaviorSubject(false);

  topicNameFilter = ""
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
  messageSqsMagic = "ㅤ"

  userconfig = new UserConfiguration()

  constructor(public subscriptionService: SnsSubscriptionService, public configService: ConfigService,
    public infoAtCursorService: InfoAtCursorService) {
    configService.load()
  }
  ngOnDestroy(): void {
    this.destroyed = true
  }

  ngOnInit(): void {
    this.configService
      .userConfiguration
      .pipe(take(1))
      .subscribe(rsp => {
        // set those defaults when this component is created
        this.endpointFilter = rsp.defaultEmail
        this.subscribeTopicRequest.endpoint = rsp.defaultEmail
      })

    this.configService
      .userConfiguration
      .pipe(takeWhile(() => !this.destroyed))
      .subscribe(rsp => {
        this.userconfig = rsp
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
    this.messageSqsMagic = "ㅤ"
    this.messageCreate = "ㅤ"
    this.messageCreateError = false
    this.freezeButtons.next(true)
    this.subscriptionService
      .subscribe(request)
      .subscribe({
        next: rsp => {
          this.subscribeTopicResult = rsp
          this.messageCreate = "Subscription was created (or already existed)!"
          if (request.protocol === "SQS")
            this.messageSqsMagic = `You can start listening to your queue <a href="/listen-to-queue/${rsp.createdQueueArn}">here</a>! `
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

  changedDropDown(value: string) {
    this.messageSqsMagic = "ㅤ"
    this.messageCreate = "ㅤ"
    this.messageCreateError = false
    if (value === "EMAIL") {
      this.subscribeTopicRequest.endpoint = this.userconfig?.defaultEmail
    } else if (value === "SQS") {
      this.subscribeTopicRequest.endpoint = this.userconfig?.aws?.userQueueArn
    }
  }
}
