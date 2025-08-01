package com.stephensantilli.totp;

public class Code {

    private String name, base32Secret, crypto, match;

    private int digits, duration;

    private boolean enabled;

    public Code(String name, String base32Secret, String match, int digits, int duration,
            String crypto,
            boolean enabled) {

        this.name = name;
        this.base32Secret = base32Secret;
        this.match = match;
        this.digits = digits;
        this.duration = duration;
        this.crypto = crypto;
        this.enabled = enabled;

    }

    public boolean equals(Code code) {

        return name.equals(code.getName())
                && base32Secret.equals(code.getBase32Secret())
                && digits == code.getDigits()
                && crypto.equals(code.getCrypto());

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String generateCode() {

        return Generator.generateTOTP(base32Secret, digits, duration, crypto);

    }

    public String getBase32Secret() {

        return base32Secret;
    }

    public void setBase32Secret(String base32Secret) {

        this.base32Secret = base32Secret;
    }

    public String getCrypto() {

        return crypto;
    }

    public void setCrypto(String crypto) {

        this.crypto = crypto;
    }

    public String getMatch() {

        return match;
    }

    public void setMatch(String regex) {

        this.match = regex;
    }

    public int getDigits() {

        return digits;
    }

    public void setDigits(int digits) {

        this.digits = digits;
    }

    public int getDuration() {

        return duration;
    }

    public void setDuration(int duration) {

        this.duration = duration;

    }

    public boolean isEnabled() {

        return enabled;
    }

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }

}
