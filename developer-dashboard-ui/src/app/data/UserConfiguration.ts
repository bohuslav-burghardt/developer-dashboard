
import { AwsConfig } from "./AwsConfig";
import { JiraConfig } from "./JiraConfig";
import { WidgetDefinition } from "./WidgetDefinition"

export class UserConfiguration {
  defaultEmail = ""
  jira = new JiraConfig()
  widgets = new Array<WidgetDefinition>()
  aws = new AwsConfig()
}
