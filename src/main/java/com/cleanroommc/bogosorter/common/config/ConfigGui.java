package com.cleanroommc.bogosorter.common.config;

import com.cleanroommc.bogosorter.BogoSortAPI;
import com.cleanroommc.bogosorter.BogoSorter;
import com.cleanroommc.bogosorter.api.SortRule;
import com.cleanroommc.bogosorter.common.sort.NbtSortRule;
import com.cleanroommc.bogosorter.common.sort.SortHandler;
import com.cleanroommc.modularui.api.ModularUITextures;
import com.cleanroommc.modularui.api.drawable.Text;
import com.cleanroommc.modularui.api.drawable.shapes.Rectangle;
import com.cleanroommc.modularui.api.math.Alignment;
import com.cleanroommc.modularui.api.math.Color;
import com.cleanroommc.modularui.api.screen.ModularWindow;
import com.cleanroommc.modularui.api.screen.UIBuildContext;
import com.cleanroommc.modularui.api.widget.Widget;
import com.cleanroommc.modularui.common.widget.*;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigGui {

    public static ModularWindow createConfigWindow(UIBuildContext buildContext) {
        buildContext.addCloseListener(BogoSorter.SERIALIZER::saveConfig);
        ModularWindow.Builder builder = ModularWindow.builder(300, 250);
        builder.setBackground(new Rectangle().setColor(Color.withAlpha(0x505050, 175)).setCornerRadius(10))
                .widget(new TextWidget("SortConfig")
                        .setDefaultColor(Color.WHITE.normal)
                        .setTextAlignment(Alignment.Center)
                        .setPos(0, 5)
                        .setSize(300, 11))
                .widget(new Rectangle().setColor(Color.WHITE.normal).asWidget()
                        .setPos(89, 16)
                        .setSize(1, 234))
                .widget(new Rectangle().setColor(Color.WHITE.normal).asWidget()
                        .setPos(0, 16)
                        .setSize(300, 1))
                .widget(new TabContainer()
                        .addTabButton(new TabButton(0)
                                .setBackground(new Rectangle().setColor(Color.BLACK.bright(3)), new Text("General").color(Color.WHITE.normal))
                                .setSize(89, 16)
                                .setPos(-90, 0))
                        .addTabButton(new TabButton(1)
                                .setBackground(new Rectangle().setColor(Color.BLACK.bright(3)), new Text("Item sort rules").color(Color.WHITE.normal))
                                .setSize(89, 16)
                                .setPos(-90, 16))
                        .addTabButton(new TabButton(2)
                                .setBackground(new Rectangle().setColor(Color.BLACK.bright(3)), new Text("NBT sort rules").color(Color.WHITE.normal))
                                .setSize(89, 16)
                                .setPos(-90, 32))
                        .addPage(createGeneralConfigUI(buildContext))
                        .addPage(createItemSortConfigUI(buildContext))
                        .addPage(createNbtSortConfigUI(buildContext))
                        .setSize(210, 236)
                        .setPos(90, 17));

        return builder.build();
    }

    private static Widget createGeneralConfigUI(UIBuildContext buildContext) {
        return new MultiChildWidget();
    }

    private static Widget createItemSortConfigUI(UIBuildContext buildContext) {
        SortableListWidget<SortRule<ItemStack>> sortableListWidget = SortableListWidget.removable(BogoSortAPI.INSTANCE.getItemSortRuleList(), SortHandler.getItemSortRules());
        Map<SortRule<ItemStack>, AvailableListItem<SortRule<ItemStack>>> widgetMap = new HashMap<>();
        for (SortRule<ItemStack> sortRule : BogoSortAPI.INSTANCE.getItemSortRuleList()) {
            AvailableListItem<SortRule<ItemStack>> listItem = new AvailableListItem<>(sortRule, new TextWidget(new Text(sortRule.getKey()).color(Color.WHITE.normal).shadow())
                    .setSize(80, 20));
            listItem.setAvailable(!SortHandler.getItemSortRules().contains(sortRule))
                    .setMoveConsumer(clickData -> sortableListWidget.addElement(sortRule))
                    .setAvailableBackground(ModularUITextures.BASE_BUTTON)
                    .setUnavailableBackground(ModularUITextures.FLUID_SLOT)
                    .setSize(90, 20);
            widgetMap.put(sortRule, listItem);
        }

        List<Widget> orderedWidgetList = BogoSortAPI.INSTANCE.getItemSortRuleList().stream().map(widgetMap::get).collect(Collectors.toList());

        return new MultiChildWidget()
                .addChild(new TextWidget(new Text("Available Sort-Rules").color(Color.WHITE.normal))
                        .setTextAlignment(Alignment.Center)
                        .setPos(5, 5)
                        .setSize(90, 18))
                .addChild(new TextWidget(new Text("Configured Sort-Rules").color(Color.WHITE.normal))
                        .setTextAlignment(Alignment.Center)
                        .setPos(105, 5)
                        .setSize(100, 18))
                .addChild(ListWidget.builder(new ArrayList<>(orderedWidgetList), (widget, index) -> widget)
                        .setPos(5, 24)
                        .setSize(90, 200))
                .addChild(sortableListWidget
                        .setWidgetCreator(sortRule -> new TextWidget(new Text(sortRule.getKey()).color(Color.WHITE.normal).shadow())
                                .setTextAlignment(Alignment.Center)
                                .setBackground(ModularUITextures.BASE_BUTTON)
                                .setSize(80, 20))
                        .setSaveFunction(list -> {
                            SortHandler.getItemSortRules().clear();
                            SortHandler.getItemSortRules().addAll(list);
                        })
                        .setOnRemoveElement(sortRule -> widgetMap.get(sortRule).setAvailable(true))
                        .setPos(105, 24)
                        .setSize(100, 200));
    }

    private static Widget createNbtSortConfigUI(UIBuildContext buildContext) {
        SortableListWidget<NbtSortRule> sortableListWidget = SortableListWidget.removable(BogoSortAPI.INSTANCE.getNbtSortRuleList(), SortHandler.getNbtSortRules());
        Map<NbtSortRule, AvailableListItem<NbtSortRule>> widgetMap = new HashMap<>();
        for (NbtSortRule sortRule : BogoSortAPI.INSTANCE.getNbtSortRuleList()) {
            AvailableListItem<NbtSortRule> listItem = new AvailableListItem<>(sortRule, new TextWidget(new Text(sortRule.getKey()).color(Color.WHITE.normal).shadow())
                    .setSize(80, 20));
            listItem.setAvailable(!SortHandler.getNbtSortRules().contains(sortRule))
                    .setMoveConsumer(clickData -> sortableListWidget.addElement(sortRule))
                    .setAvailableBackground(ModularUITextures.BASE_BUTTON)
                    .setUnavailableBackground(ModularUITextures.FLUID_SLOT)
                    .setSize(90, 20);
            widgetMap.put(sortRule, listItem);
        }

        List<Widget> orderedWidgetList = BogoSortAPI.INSTANCE.getNbtSortRuleList().stream().map(widgetMap::get).collect(Collectors.toList());

        return new MultiChildWidget()
                .addChild(ListWidget.builder(new ArrayList<>(orderedWidgetList), (widget, index) -> widget)
                        .setPos(5, 15)
                        .setSize(90, 200))
                .addChild(sortableListWidget
                        .setWidgetCreator(sortRule -> new TextWidget(new Text(sortRule.getKey()).color(Color.WHITE.normal).shadow())
                                .setTextAlignment(Alignment.Center)
                                .setBackground(ModularUITextures.BASE_BUTTON)
                                .setSize(80, 20))
                        .setSaveFunction(list -> {
                            SortHandler.getNbtSortRules().clear();
                            SortHandler.getNbtSortRules().addAll(list);
                        })
                        .setOnRemoveElement(sortRule -> widgetMap.get(sortRule).setAvailable(true))
                        .setPos(105, 15)
                        .setSize(100, 200));
    }

    private static Widget createOrePrefixConfigUI(UIBuildContext buildContext) {
        return new MultiChildWidget();
    }
}