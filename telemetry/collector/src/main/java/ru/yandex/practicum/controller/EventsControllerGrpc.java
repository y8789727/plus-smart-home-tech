package ru.yandex.practicum.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc.CollectorControllerImplBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.service.EventsService;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class EventsControllerGrpc extends CollectorControllerImplBase {
    private final EventsService eventsService;

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("GRPC collectSensorEvent event.id={}", request.getId());
        try {
            eventsService.collectServiceEvent(request);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.INTERNAL.withDescription(e.getLocalizedMessage()).withCause(e)));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("GRPC collectHubEvent event.type={} from hub id={}", request.getPayloadCase(), request.getHubId());
        try {
            eventsService.collectHubEvent(request);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.INTERNAL.withDescription(e.getLocalizedMessage()).withCause(e)));
        }
    }
}
