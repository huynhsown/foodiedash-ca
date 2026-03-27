package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.command.SearchDriversCommand;
import com.ute.foodiedash.application.user.query.SearchDriverQueryResult;
import com.ute.foodiedash.domain.common.model.PageResult;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchDriversUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PageResult<SearchDriverQueryResult> execute(SearchDriversCommand command) {
        PageResult<User> page = userRepository.searchDrivers(
                command.keyword(),
                command.userStatus(),
                command.driverVerificationStatus(),
                command.vehicleType(),
                command.createdFrom(),
                command.createdTo(),
                command.page(),
                command.size(),
                command.sortBy(),
                command.sortDirection()
        );

        return new PageResult<>(
                page.getContent().stream()
                        .map(SearchDriverQueryResult::from)
                        .toList(),
                page.getPage(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
