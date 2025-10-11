package com.bikeshare.api.profile;

public record UpdateOwnerProfileRequest(
        String fullName,
        String phone,
        String publicBio,
        String avatarUrl,
        String payoutEmail,
        String bankAccountNumber,
        String yapePhoneNumber
) {}