package im.ghosty.catboyaddons.utils.events

import cc.polyfrost.oneconfig.events.event.CancellableEvent
import net.minecraft.network.Packet

class InventoryCloseEvent() : CancellableEvent()

class WorldChangeEvent(val load: Boolean)

class PacketReceiveEvent(val packet: Packet<*>) : CancellableEvent()
class PacketSendEvent(val packet: Packet<*>) : CancellableEvent()