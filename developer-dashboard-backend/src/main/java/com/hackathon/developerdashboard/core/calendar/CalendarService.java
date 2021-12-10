package com.hackathon.developerdashboard.core.calendar;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.hackathon.developerdashboard.core.configuration.service.ConfigurationService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static com.hackathon.developerdashboard.core.calendar.AuthCheckResult.AuthCheckState.AUTH_REQUIRED;
import static com.hackathon.developerdashboard.core.calendar.AuthCheckResult.AuthCheckState.OK;

@Service
public class CalendarService {
    private final ConfigurationService configurationService;
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String userId = "developer-dashboard-user-id-3";
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_READONLY);
    // private static final String CREDENTIALS_FILE_PATH = "/calendarClientSecret.json";

    private String roEventCalenarId;

    public CalendarService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        GoogleAuthorizationCodeFlow flow = createFlow();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(9092).build();

        Credential credential = flow.loadCredential(userId);
        if (credential != null
                && (credential.getRefreshToken() != null
                || credential.getExpiresInSeconds() == null
                || credential.getExpiresInSeconds() > 60)) {
            return credential;
        }

        //System.out.println("xx  " + receiver.getRedirectUri());
        String redirectUri = receiver.getRedirectUri();
        AuthorizationCodeRequestUrl authorizationUrl =
                flow.newAuthorizationUrl().setRedirectUri(redirectUri);
        String url = authorizationUrl.build();
        System.out.println("your url: " + url);
        Preconditions.checkNotNull(url);
        new AuthorizationCodeInstalledApp.DefaultBrowser().browse(url);
        // receive authorization code and exchange it for an access token
        String code = receiver.waitForCode();
        TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();


        return flow.createAndStoreCredential(response, userId);

        // return newCredential(userId).setFromTokenResponse(response);
    }

    private Calendar getCalendarClient() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public List<Event> getEvents() throws Exception {
        Calendar calendarClient = getCalendarClient();

        CalendarListEntry calendarListEntry = calendarClient
                .calendarList()
                .list()
                .execute()
                .getItems()
                .stream()
                .filter(c -> "Rollouts".equals(c.getSummary()) || "Application Rollouts".equals(c.getDescription()))
                .findFirst()
                .orElse(null);

        roEventCalenarId = calendarListEntry.getId();
        DateTime now = new DateTime(System.currentTimeMillis());

        return calendarClient.events().list(calendarListEntry.getId())
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()
                .getItems();
    }

    public void reserve(String eventId, String description) throws Exception {
        Calendar calendarClient = getCalendarClient();
        Event event = calendarClient.events().get(roEventCalenarId, eventId).execute();
        event.setSummary(description);
        calendarClient.events().update(roEventCalenarId, eventId, event).execute();
    }

    public AuthCheckResult checkCalendarAuth() throws Exception {
        GoogleAuthorizationCodeFlow flow = createFlow();

        Credential credential = flow.loadCredential(userId);
        if (credential != null
                && (credential.getRefreshToken() != null
                || credential.getExpiresInSeconds() == null
                || credential.getExpiresInSeconds() > 60)) {
            return new AuthCheckResult().setAuthCheckState(OK);
        }

        AuthorizationCodeRequestUrl authorizationUrl =
                flow.newAuthorizationUrl().setRedirectUri(getRedirectUri());
        String url = authorizationUrl.build();

        return new AuthCheckResult().setAuthCheckState(AUTH_REQUIRED).setAuthUrl(url);
    }

    public void receiveCallback(String code) throws Exception {
        GoogleAuthorizationCodeFlow flow = createFlow();
        TokenResponse response = flow.newTokenRequest(code).setRedirectUri(getRedirectUri()).execute();
        flow.createAndStoreCredential(response, userId);
    }

    private static GoogleAuthorizationCodeFlow createFlow() throws Exception {
        InputStream in = new ClassPathResource("calendarClientSecret.json").getInputStream();

        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        return new GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
    }

    private String getRedirectUri() {
        return "http://localhost:" + configurationService.getPort() + "/api/ro-calender/callback";
    }
}