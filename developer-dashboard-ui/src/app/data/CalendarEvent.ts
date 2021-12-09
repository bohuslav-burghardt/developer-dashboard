import { CalendarDateTimeContainer } from "./CalendarDateTimeContainer"

export class CalendarEvent {
  id = ""
  end = new CalendarDateTimeContainer()
  start = new CalendarDateTimeContainer()
  summary = ""
  htmlLink = ""
}
