/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.email;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class used to build an email
 *
 * @author Tim van Ekert
 */
public class IEmail {

    private List<String> recipients;
    private String subject;
    private String content;
    private boolean useHTML;
    private List<File> attachments;

    public IEmail(String subject, String content, boolean useHTML, String... recipients) {
        this.subject = subject;
        this.content = content;
        this.useHTML = useHTML;
        this.recipients = Arrays.asList(recipients);
        this.attachments = new ArrayList<>();
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public IEmail setAttachments(File... attachments) {
        this.attachments = Arrays.asList(attachments);
        return this;
    }

    public IEmail addAttachments(File... attachments) {
        this.attachments.addAll(Arrays.asList(attachments));
        return this;
    }

    public IEmail addRecipient(String address) {
        recipients.add(address);
        return this;
    }

    public IEmail removeRecipient(String address) {
        recipients.remove(address);
        return this;
    }

    public IEmail setRecipients(String... recipients) {
        this.recipients = Arrays.asList(recipients);
        return this;
    }

    public IEmail clearRecipients() {
        recipients.clear();
        return this;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    /**
     * This method is used to replace parameters in the email's content
     * @param ps An implementation of {@link ParameterSetter}
     * @return The email instance
     */
    public IEmail setParameters(ParameterSetter ps) {
        content = ps.run(content);
        return this;
    }

    /**
     * Sets the email's content to that of a HTML file
     * @param url The URL to the file
     * @param clear Whether to clear the previous content or not
     * @return The email instance
     */
    public IEmail setContentFromURL(URL url, boolean clear) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.
                openStream()))) {
            String line;
            content = "";
            while ((line = br.readLine()) != null) {
                content += line + "\n";
            }
        } catch (IOException ex) {
            Logger.getLogger(IEmail.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public IEmail addLine(String line) {
        content += "\nline";
        return this;
    }

    public IEmail setContent(String content) {
        this.content = content;
        return this;
    }

    public String getContent() {
        return content;
    }

    public IEmail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public IEmail setUseHTML(boolean useHTML) {
        this.useHTML = useHTML;
        return this;
    }

    public boolean usesHTML() {
        return useHTML;
    }

    public IEmail(String subject, boolean useHTML, String... recipients) {
        this(subject, "", useHTML, recipients);
    }

    public IEmail(String subject, String... recipients) {
        this(subject, "", false, recipients);
    }

    public IEmail(String... recipients) {
        this("", "", false, recipients);
    }

    public interface ParameterSetter {
        String run(String txt);
    }

}
