package org.inventivetalent.hypixel.fairysoulhelper;

import com.google.common.base.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class SpawnListener {

	private FairySoulMod mod;

	private int tick    = 1;
	private int seconds = 0;

	private Set<UUID> allFairySouls = new LinkedHashSet<>();
	private Set<UUID> foundFairySouls = new LinkedHashSet<>();

	public SpawnListener(FairySoulMod mod) {
		this.mod = mod;
	}

	@SubscribeEvent
	public void on(EntityJoinWorldEvent event) {
		if (event.getEntity() == Minecraft.getMinecraft().player) {
			System.out.println("Joined World!");
			allFairySouls.clear();
		}
	}

	@SubscribeEvent
	public void on(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			if (tick >= 20) {

				tick = 1;
				seconds++;

				ArrayDeque<Particle>[][] fxLayers = ObfuscationReflectionHelper.getPrivateValue(ParticleManager.class, Minecraft.getMinecraft().effectRenderer, "fxLayers", "field_78876_b");;
				Field posXField = null;
				Field posYField = null;
				Field posZField = null;

				World world = Minecraft.getMinecraft().world;
				if (world != null) {

					ScorePlayerTeam newTeam = world.getScoreboard().getTeam("newFairySouls");
					if (newTeam == null) {
						newTeam = Minecraft.getMinecraft().world.getScoreboard().createTeam("newFairySouls");
					}
					newTeam.setColor(TextFormatting.GREEN);
					newTeam.setPrefix(TextFormatting.GREEN.toString());

					ScorePlayerTeam oldTeam = Minecraft.getMinecraft().world.getScoreboard().getTeam("oldFairySouls");
					if (oldTeam == null) {
						oldTeam = Minecraft.getMinecraft().world.getScoreboard().createTeam("oldFairySouls");
					}
					oldTeam.setColor(TextFormatting.DARK_GRAY);
					oldTeam.setPrefix(TextFormatting.DARK_GRAY.toString());

					boolean[] b = new boolean[1];

					for (Entity entity : Minecraft.getMinecraft().world.getEntities(EntityArmorStand.class, new Predicate<EntityArmorStand>() {
						@Override
						public boolean apply(@Nullable EntityArmorStand input) {
							return true;
						}
					})) {
						if (entity instanceof EntityArmorStand) {
							//						System.out.println("armor stand!");
							EntityArmorStand armorStand = (EntityArmorStand) entity;

							//							System.out.println(entity);

							ItemStack headItem = armorStand.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
							//							System.out.println(headItem);

							if (headItem != null) {
								Item item = headItem.getItem();
								if (item == Items.SKULL && item.getMetadata(headItem) == 3) {
									//									System.out.println(headItem.getTagCompound());

									if (headItem.getTagCompound() != null) {
										NBTTagCompound skullOwner = headItem.getTagCompound().getCompoundTag("SkullOwner");
										if ("57a4c8dc-9b8e-3d41-80da-a608901a6147".equals(skullOwner.getString("Id"))) {
											//											System.out.println("Fairy Soul!");

											if (allFairySouls.add(armorStand.getUniqueID())) {
												System.out.println(allFairySouls);
												System.out.println(allFairySouls.size()+" unique fairy souls");
											}

											armorStand.setAlwaysRenderNameTag(false);
											armorStand.setInvisible(false);
											armorStand.setGlowing(true);
											//											armorStand.setCustomNameTag("FAIRY SOUL!!!!");

											if (fxLayers != null) {
												for (int i = 0; i < fxLayers.length; i++) {
													ArrayDeque<Particle>[] a = fxLayers[i];
													for (int k = 0; k < a.length; k++) {
														ArrayDeque<Particle> particles = a[k];

														for (Particle particle : particles) {
															try {
																if (posXField != null && posYField != null && posZField != null) {
																	double x =(double)ObfuscationReflectionHelper.getPrivateValue(Particle.class,particle,"posX","field_187126_f");
																	double y =(double)ObfuscationReflectionHelper.getPrivateValue(Particle.class,particle,"posY","field_187127_g");
																	double z = (double)ObfuscationReflectionHelper.getPrivateValue(Particle.class,particle,"posZ","field_187128_h");

																	double d = armorStand.getDistance(x, y, z);
																	if (d < 2.5) {
																		//																		System.out.println(particle);
																		armorStand.setAlwaysRenderNameTag(true);

																		if (particle instanceof ParticlePortal) {
																			//																		System.out.println(x + "," + y + "," + z);
																			armorStand.setFire(2);
																			armorStand.setCustomNameTag("FAIRY SOUL!!!!");


																			Minecraft.getMinecraft().world.getScoreboard().addPlayerToTeam(armorStand.getCachedUniqueIdString(), "newFairySouls");

																			b[0] = true;
																		} else {
																			armorStand.setCustomNameTag("already found :(");

																			Minecraft.getMinecraft().world.getScoreboard().addPlayerToTeam(armorStand.getCachedUniqueIdString(), "oldFairySouls");

																			if (foundFairySouls.add(armorStand.getUniqueID())) {
																				System.out.println(foundFairySouls);
																				System.out.println(foundFairySouls.size()+" found fairy souls");
																			}

																		}
																	} else {
																	}
																}
															} catch (Exception e) {
																e.printStackTrace();
															}
														}
													}
												}
											}
										}
									}
								}

							}
						}
					}

					if (b[0]) {
						Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, new TextComponentString("There's an undiscovered Fairy Soul near you!"));
					}
				}
			}
			tick++;

		}
	}

}
