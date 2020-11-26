package ru.alfomine.afmsm.space;

import cr0s.warpdrive.Commons;
import cr0s.warpdrive.WarpDrive;
import cr0s.warpdrive.config.InvalidXmlException;
import cr0s.warpdrive.data.CelestialObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;

public class PlanetRenderData {

    public float red;
    public float green;
    public float blue;
    public float alpha;
    public String texture;
    public ResourceLocation resourceLocation;
    public double periodU;
    public double periodV;
    public boolean isAdditive;

    public PlanetRenderData(final String location, final Element elementRender) throws InvalidXmlException {
        try {
            red = Commons.clamp(0.0F, 1.0F, Float.parseFloat(elementRender.getAttribute("red")));
            green = Commons.clamp(0.0F, 1.0F, Float.parseFloat(elementRender.getAttribute("green")));
            blue = Commons.clamp(0.0F, 1.0F, Float.parseFloat(elementRender.getAttribute("blue")));
            alpha = Commons.clamp(0.0F, 1.0F, Float.parseFloat(elementRender.getAttribute("alpha")));
        } catch (final Exception exception) {
            exception.printStackTrace(WarpDrive.printStreamError);
            WarpDrive.logger.error(String.format("Exception while parsing Render element RGBA attributes at %s",
                    location ));
            red = 0.5F;
            green = 0.5F;
            blue = 0.5F;
            alpha = 0.5F;
        }
        texture = elementRender.getAttribute("texture");
        if (texture == null || texture.isEmpty()) {
            texture = null;
            resourceLocation = null;
            periodU = 1.0D;
            periodV = 1.0D;
            isAdditive = false;
        } else {
            resourceLocation = new ResourceLocation(texture);

            periodU = 0.001D;
            final String stringPeriodU = elementRender.getAttribute("periodU");
            if (!stringPeriodU.isEmpty()) {
                try {
                    periodU = Commons.clampMantisse(0.001D, 1000000.0D, Double.parseDouble(stringPeriodU));
                } catch (final NumberFormatException exception) {
                    throw new InvalidXmlException(String.format("Invalid periodU attribute '%s' at %s",
                            stringPeriodU, location ));
                }
            }

            periodV = 0.001D;
            final String stringPeriodV = elementRender.getAttribute("periodV");
            if (!stringPeriodV.isEmpty()) {
                try {
                    periodV = Commons.clampMantisse(0.001D, 1000000.0D, Double.parseDouble(stringPeriodV));
                } catch (final NumberFormatException exception) {
                    throw new InvalidXmlException(String.format("Invalid periodV attribute '%s' at %s",
                            stringPeriodV, location ));
                }
            }

            isAdditive = Boolean.parseBoolean(elementRender.getAttribute("additive"));
        }
    }

    public PlanetRenderData(final NBTTagCompound tagCompound) {
        readFromNBT(tagCompound);
    }

    public PlanetRenderData(CelestialObject.RenderData warpRenderData) {
        red = warpRenderData.red;
        green = warpRenderData.green;
        blue = warpRenderData.blue;
        alpha = warpRenderData.alpha;
        texture = warpRenderData.texture;
        resourceLocation = warpRenderData.resourceLocation;
        periodU = warpRenderData.periodU;
        periodV = warpRenderData.periodV;
        isAdditive = warpRenderData.isAdditive;
    }

    public void readFromNBT(@Nonnull final NBTTagCompound tagCompound) {
        red = tagCompound.getFloat("red");
        green = tagCompound.getFloat("green");
        blue = tagCompound.getFloat("blue");
        alpha = tagCompound.getFloat("alpha");
        texture = tagCompound.getString("texture");
        if (texture.isEmpty()) {
            texture = null;
            resourceLocation = null;
            periodU = 1.0D;
            periodV = 1.0D;
            isAdditive = false;
        } else {
            resourceLocation = new ResourceLocation(texture);
            periodU = tagCompound.getDouble("periodU");
            periodV = tagCompound.getDouble("periodV");
            isAdditive = tagCompound.getBoolean("isAdditive");
        }
    }

    public NBTTagCompound writeToNBT(@Nonnull final NBTTagCompound tagCompound) {
        tagCompound.setFloat("red", red);
        tagCompound.setFloat("green", green);
        tagCompound.setFloat("blue", blue);
        tagCompound.setFloat("alpha", alpha);
        if (texture != null) {
            tagCompound.setString("texture", texture);
            tagCompound.setDouble("periodU", periodU);
            tagCompound.setDouble("periodV", periodV);
            tagCompound.setBoolean("isAdditive", isAdditive);
        }
        return tagCompound;
    }
}