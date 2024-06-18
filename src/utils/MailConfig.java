package utils;

public class MailConfig {
	private final String smtpHost;
    private final String smtpPort;
    private final String smtpAuth;
    private final String smtpSocketFactoryClass;
    private final String username;
    private final String password;

    public MailConfig(String smtpHost, String smtpPort, String smtpAuth, String smtpSocketFactoryClass, String username, String password) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpAuth = smtpAuth;
        this.smtpSocketFactoryClass = smtpSocketFactoryClass;
        this.username = username;
        this.password = password;
    }
    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public String getSmtpAuth() {
        return smtpAuth;
    }

    public String getSmtpSocketFactoryClass() {
        return smtpSocketFactoryClass;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
