package net.jonko0493.computercartographer.peripheral;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class CartographyPeripheral implements IPeripheral {
    @Override
    public String getType() {
        return "cartographer";
    }

    @Override
    public Set<String> getAdditionalTypes() {
        return IPeripheral.super.getAdditionalTypes();
    }

    @Override
    public void attach(IComputerAccess computer) {
        IPeripheral.super.attach(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        IPeripheral.super.detach(computer);
    }

    @Nullable
    @Override
    public Object getTarget() {
        return IPeripheral.super.getTarget();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return false;
    }
}
