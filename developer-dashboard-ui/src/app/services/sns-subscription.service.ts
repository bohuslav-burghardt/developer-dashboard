import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { SubscribeTopicRequest } from '../data/SubscribeTopicRequest';
import { UnsubscribeTopicRequest } from '../data/UnsubscribeTopicRequest';
import { SubscribeTopicResult } from '../data/SubscribeTopicResult';
import { UnsubscribeTopicResult } from '../data/UnsubscribeTopicResult';
import { ListSubscriptionResult } from '../data/ListSubscriptionResult';
import { Subscription } from '../data/Subscription';

@Injectable({
  providedIn: 'root'
})
export class SnsSubscriptionService {

  constructor(private http: HttpClient) {

  }

  public load(topicName: string, region: string, protocol: string, endpoint: string) {
    let params = new HttpParams();
    if (topicName)
      params = params.set('topicName', topicName)
    if (region)
      params = params.set('region', region)
    if (protocol)
      params = params.set('protocol', protocol)
    if (endpoint)
      params = params.set('endpoint', endpoint)

    return this.http.get<ListSubscriptionResult>(`api/widgets/sns-subscription`, { params })
  }

  public subscribe(request: SubscribeTopicRequest) {
    return this.http.post<SubscribeTopicResult>(`api/widgets/sns-subscription`, request)
  }
  public unsubscribe(request: UnsubscribeTopicRequest) {
    return this.http.request<UnsubscribeTopicResult>('delete', `api/widgets/sns-subscription`, { body: request })
  }
}
