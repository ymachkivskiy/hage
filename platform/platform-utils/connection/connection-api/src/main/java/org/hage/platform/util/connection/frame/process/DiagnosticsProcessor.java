package org.hage.platform.util.connection.frame.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.util.connection.frame.Header;
import org.hage.platform.util.connection.frame.diagnostics.Diagnostics;

import static lombok.AccessLevel.PRIVATE;
import static org.hage.platform.util.connection.frame.diagnostics.Diagnostics.SUCCESS_DIAGNOSTICS;

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
        return new DiagnosticsProcessor(SUCCESS_DIAGNOSTICS);
    }

    public static DiagnosticsProcessor withDiagnostics(Diagnostics diagnostics) {
        return new DiagnosticsProcessor(diagnostics);
    }

}
