import { HttpClient } from '@angular/common/http';
import { collectExternalReferences } from '@angular/compiler';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SqsMessage } from 'src/app/data/SqsMessage';
import { InfoAtCursorService } from 'src/app/services/info-at-cursor.service';

@Component({
  selector: 'app-listen-to-queue',
  templateUrl: './listen-to-queue.component.html',
  styleUrls: ['./listen-to-queue.component.scss']
})
export class ListenToQueueComponent implements OnInit, OnDestroy {
  destroyed = false
  queue = ""
  messages: SqsMessage[] = []
  constructor(private route: ActivatedRoute, private httpClient: HttpClient, public infoAtCursorService: InfoAtCursorService) { }
  ngOnDestroy(): void {
    this.destroyed = true
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.queue = params["queue"]
      console.log("queue: " + this.queue)
    });
    this.runPoll()
  }

  runPoll() {
    console.log("Running SQS polling")
    if (this.destroyed) {
      console.log("ListenToQueueComponent was destroyed, stopping polling")
      return
    }

    this.httpClient.get<SqsMessage[]>(`api/widgets/sns-subscription/${this.queue}/messages`)
      .subscribe({
        next: rsp => rsp.forEach(m => {
          this.messages.unshift(m)
        })
      })
      .add(() => {
        setTimeout(() => this.runPoll(), 1000)
      })
  }
  clear() {
    this.messages.length = 0
  }




}
