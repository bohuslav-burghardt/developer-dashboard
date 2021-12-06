export class SubscribeTopicResult {
  region = ""
  topicArn = ""
  subscriptionArns = new Array<string>()
  wasAlreadySubscribed = false
}
