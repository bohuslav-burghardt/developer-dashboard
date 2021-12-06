export class UnsubscribeTopicResult {
  region = ""
  topicArn = ""
  subscriptionArns = new Array<string>()
  wasAlreadyUnsubscribed = false
}
