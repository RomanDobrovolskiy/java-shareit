package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID_HEADER;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader(USER_ID_HEADER) @NotNull Long requesterId,
                                        @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Created request " + itemRequestDto + " with id " + requesterId);
        ItemRequest itemRequest = ItemRequestMapper.fromDto(itemRequestDto);
        return ItemRequestMapper.toDto(itemRequestService.createRequest(itemRequest, requesterId));
    }

    @GetMapping
    public List<ItemRequestDto> getAllUserRequests(@RequestHeader(USER_ID_HEADER) @NotNull Long requesterId) {
        log.info("Getting all user requests with id " + requesterId);
        return ItemRequestMapper.toDtoList(itemRequestService.getAllUserRequests(requesterId));
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader(USER_ID_HEADER) @NotNull Long userId,
                                         @PathVariable Long requestId) {
        log.info("Getting request by id " + requestId + " and userId " + userId);
        return ItemRequestMapper.toDto(itemRequestService.getRequestById(requestId, userId));
    }

    @GetMapping("all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(USER_ID_HEADER) @NotNull Long userId,
                                               @PositiveOrZero @RequestParam(name = "from", required = false,
                                                       defaultValue = "0") Integer from,
                                               @Positive @RequestParam(name = "size", required = false,
                                                       defaultValue = "10") Integer size) {
        log.info("Getting all requests with userId " + userId + " with " + from +
                " and size " + size);
        return ItemRequestMapper.toDtoList(itemRequestService.getAllRequests(userId, from, size));
    }
}
