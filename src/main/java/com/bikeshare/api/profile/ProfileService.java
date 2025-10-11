package com.bikeshare.api.profile;

import com.bikeshare.api.user.User;
import com.bikeshare.api.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final OwnerProfileRepository ownerProfileRepo;
    private final RenterProfileRepository renterProfileRepo;
    private final UserRepository userRepo; // Necesitamos el repositorio de User

    @Transactional
    public void createProfileForUser(User user) {
        if (user.isOwner()) {
            if (ownerProfileRepo.findByUserId(user.getId()).isEmpty()) {
                OwnerProfile ownerProfile = OwnerProfile.builder().user(user).isVerified(false).build();
                ownerProfileRepo.save(ownerProfile);
            }
        } else {
            if (renterProfileRepo.findByUserId(user.getId()).isEmpty()) {
                RenterProfile renterProfile = RenterProfile.builder().user(user).notificationsEnabled(true).build();
                renterProfileRepo.save(renterProfile);
            }
        }
    }

    public ProfileResponse getProfileByUserId(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (user.isOwner()) {
            OwnerProfile profile = ownerProfileRepo.findByUserId(userId)
                    .orElseThrow(() -> new IllegalStateException("Perfil de propietario no encontrado"));
            return ProfileResponse.from(user, profile);
        } else {
            RenterProfile profile = renterProfileRepo.findByUserId(userId)
                    .orElseThrow(() -> new IllegalStateException("Perfil de arrendatario no encontrado"));
            return ProfileResponse.from(user, profile);
        }
    }
}