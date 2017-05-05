package org.hage.platform.cluster.connection.frame.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.connection.frame.diagnostics.Diagnostics;
import org.hage.platform.cluster.connection.frame.Header;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class DiagnosticsProcessor extends AbstractHeaderProcessor {

    private final Diagnostics diagnostics;

    @Override
    protected void updateHeader(Header.HeaderBuilder mutableHeader) {
        log.trace("Set diagnostics to {} for header {}", diagnostics, mutableHeader);

        mutableHeader.diagnostics(diagnostics);
    }

    public static DiagnosticsProcessor successful() {
        return new DiagnosticsProcessor(Diagnostics.SUCCESS_DIAGNOSTICS);
    }

    public static DiagnosticsProcessor withDiagnostics(Diagnostics diagnostics) {
        return new DiagnosticsProcessor(diagnostics);
    }

}
