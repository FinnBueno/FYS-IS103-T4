/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author finnb
 */
public class Email {

    private List<String> recipients;
    private String subject;
    private String content;
    private boolean useHTML;

    public Email(String subject, String content, boolean useHTML, String... recipients) {
	this.subject = subject;
	this.content = content;
	this.useHTML = useHTML;
	this.recipients = Arrays.asList(recipients);
    }

    public Email addRecipient(String address) {
	recipients.add(address);
	return this;
    }

    public Email removeRecipient(String address) {
	recipients.remove(address);
	return this;
    }

    public Email setRecipients(String... recipients) {
	this.recipients = Arrays.asList(recipients);
	return this;
    }

    public Email clearRecipients() {
	recipients.clear();
	return this;
    }

    public List<String> getRecipients() {
	return recipients;
    }

    public Email setContentFromURL(URL url, boolean clear) {
	try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
	    String line;
	    content = "";
	    while ((line = br.readLine()) != null) {
		content += line + "\n";
	    }
	} catch (IOException ex) {
	    Logger.getLogger(Email.class.getName()).
		    log(Level.SEVERE, null, ex);
	}
	return this;
    }

    public Email addLine(String line) {
	content += "\nline";
	return this;
    }

    public Email setContent(String content) {
	this.content = content;
	return this;
    }

    public String getContent() {
	return content;
    }

    public Email setSubject(String subject) {
	this.subject = subject;
	return this;
    }

    public String getSubject() {
	return subject;
    }

    public Email setUseHTML(boolean useHTML) {
	this.useHTML = useHTML;
	return this;
    }

    public boolean usesHTML() {
	return useHTML;
    }

    public Email(String subject, boolean useHTML, String... recipients) {
	this(subject, "", useHTML, recipients);
    }

    public Email(String subject, String... recipients) {
	this(subject, "", false, recipients);
    }

    public Email(String... recipients) {
	this("", "", false, recipients);
    }

}
