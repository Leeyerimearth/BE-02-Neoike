package prgrms.neoike.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prgrms.neoike.controller.dto.drawdto.DrawSaveRequest;
import prgrms.neoike.controller.mapper.DrawMapper;
import prgrms.neoike.service.DrawService;
import prgrms.neoike.service.dto.drawdto.DrawResponse;
import prgrms.neoike.service.dto.drawdto.ServiceDrawSaveDto;
import prgrms.neoike.service.dto.drawticketdto.DrawTicketsResponse;

import javax.validation.Valid;
import java.net.URI;

import static java.text.MessageFormat.format;


@RestController
@RequestMapping("/api/v1/draws")
@RequiredArgsConstructor
public class DrawController {
    private final DrawService drawService;
    private final DrawMapper drawMapper;

    @PostMapping
    public ResponseEntity<DrawResponse> saveDraw(
            @Valid @RequestBody DrawSaveRequest saveRequest
    ) {
        ServiceDrawSaveDto serviceDrawSaveDto = drawMapper.toDrawSaveDto(saveRequest);
        DrawResponse drawResponse = drawService.save(serviceDrawSaveDto);

        URI location = URI.create(format("/api/v1/draws/{0}", drawResponse.drawId()));
        return ResponseEntity
                .created(location)
                .body(drawResponse);
    }

    @PostMapping("/win")
    public ResponseEntity<DrawTicketsResponse> winDraw(
            @RequestParam Long drawId
    ) {
        DrawTicketsResponse winningTicketsResponse = drawService.drawWinner(drawId);

        return ResponseEntity
                .ok()
                .body(winningTicketsResponse);
    }
}
