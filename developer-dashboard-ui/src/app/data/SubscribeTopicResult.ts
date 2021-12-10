export class SubscribeTopicResult {
  region = ""
  topicArn = ""
  subscriptionArns: string[] = []
  wasAlreadySubscribed = false
  createdQueueArn = ""
}
