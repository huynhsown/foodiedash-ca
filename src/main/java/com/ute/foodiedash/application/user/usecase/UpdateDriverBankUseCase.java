package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.command.UpdateDriverBankCommand;
import com.ute.foodiedash.application.user.query.DriverProfileQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateDriverBankUseCase {

    private final UserRepository userRepository;

    @Transactional
    public DriverProfileQueryResult execute(UpdateDriverBankCommand command, Long userId) {
        User user = userRepository.findByIdWithProfile(userId)
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        if (!user.isDriver()) {
            throw new BadRequestException("User is not driver");
        }

        user.getDriverProfile().updateBankInfo(
                command.bankName(),
                command.bankAccount(),
                command.bankHolderName()
        );

        user = userRepository.save(user);

        return DriverProfileQueryResult.from(user);
    }

}
