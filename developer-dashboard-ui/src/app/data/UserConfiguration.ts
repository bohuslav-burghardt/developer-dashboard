import { JiraConfig } from "./Jiraconfig"
import { WidgetDefinition } from "./WidgetDefinition"

export class UserConfiguration {
  defaultEmail = ""
  jira = new JiraConfig()
  widgets = new Array<WidgetDefinition>();
}
