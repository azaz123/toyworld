package org.toyworld.api;
import java.nio.channels.SelectionKey;

public interface service {
    public void Excute(serviceparams params);
    public void BindProtocol(protocol pro);
}
