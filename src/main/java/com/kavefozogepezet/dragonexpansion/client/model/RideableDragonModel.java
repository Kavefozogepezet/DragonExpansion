package com.kavefozogepezet.dragonexpansion.client.model;// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.kavefozogepezet.dragonexpansion.common.entities.RideableDragonEntity;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Vector3d;

public class RideableDragonModel<T extends RideableDragonEntity> extends AgeableModel<T> {
	private final ModelRenderer empty;
	private final ModelRenderer rotate;
	private final ModelRenderer childNeck;
	private final ModelRenderer head2;
	private final ModelRenderer jaw2;
	private final ModelRenderer mirrored2;
	private final ModelRenderer neck;
	private final ModelRenderer spine;
	private final ModelRenderer head;
	private final ModelRenderer jaw;
	private final ModelRenderer mirrored;
	private final ModelRenderer body;
	private final ModelRenderer left_wing;
	private final ModelRenderer left_wing_middle;
	private final ModelRenderer left_wing_tip;
	private final ModelRenderer right_wing;
	private final ModelRenderer right_wing_middle;
	private final ModelRenderer right_wing_tip;
	private final ModelRenderer tale;
	private final ModelRenderer spine4;
	private final ModelRenderer spine5;
	private final ModelRenderer spine6;
	private final ModelRenderer front_left_leg;
	private final ModelRenderer front_left_shin;
	private final ModelRenderer front_left_foot;
	private final ModelRenderer back_left_leg;
	private final ModelRenderer back_left_shin;
	private final ModelRenderer back_left_foot;
	private final ModelRenderer front_right_leg;
	private final ModelRenderer front_right_shin;
	private final ModelRenderer front_right_foot;
	private final ModelRenderer back_right_leg;
	private final ModelRenderer back_right_shin;
	private final ModelRenderer back_right_foot;

	public RideableDragonModel() {
		texWidth = 256;
		texHeight = 256;

		empty = new ModelRenderer(this);
		empty.setPos(0.0F, 24.0F, 0.0F);
		

		rotate = new ModelRenderer(this);
		rotate.setPos(0.0F, -46.0F, -22.0F);
		empty.addChild(rotate);
		

		childNeck = new ModelRenderer(this);
		childNeck.setPos(0.0F, 13.0F, -6.0F);
		rotate.addChild(childNeck);
		

		head2 = new ModelRenderer(this);
		head2.setPos(0.0F, 1.0F, 0.0F);
		childNeck.addChild(head2);
		head2.texOffs(176, 44).addBox(-6.0F, -2.0F, -30.0F, 12.0F, 5.0F, 16.0F, 0.0F, false);
		head2.texOffs(112, 30).addBox(-8.0F, -9.0F, -16.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);
		head2.texOffs(0, 0).addBox(3.0F, -13.0F, -10.0F, 2.0F, 4.0F, 6.0F, 0.0F, false);
		head2.texOffs(112, 0).addBox(3.0F, -4.0F, -28.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);

		jaw2 = new ModelRenderer(this);
		jaw2.setPos(0.0F, 3.0F, -16.0F);
		head2.addChild(jaw2);
		jaw2.texOffs(176, 65).addBox(-6.0F, 0.0F, -14.0F, 12.0F, 4.0F, 16.0F, 0.0F, false);

		mirrored2 = new ModelRenderer(this);
		mirrored2.setPos(0.0F, 30.0F, 55.0F);
		head2.addChild(mirrored2);
		mirrored2.texOffs(0, 0).addBox(-5.0F, -43.0F, -65.0F, 2.0F, 4.0F, 6.0F, 0.0F, true);
		mirrored2.texOffs(112, 0).addBox(-5.0F, -34.0F, -83.0F, 2.0F, 2.0F, 4.0F, 0.0F, true);

		neck = new ModelRenderer(this);
		neck.setPos(0.0F, 15.0F, -6.0F);
		rotate.addChild(neck);
		neck.texOffs(114, 146).addBox(-7.0F, -6.0F, -10.0F, 14.0F, 12.0F, 10.0F, 0.0F, false);
		neck.texOffs(48, 0).addBox(-1.0F, -10.0F, -8.0F, 2.0F, 4.0F, 6.0F, 0.0F, false);

		spine = new ModelRenderer(this);
		spine.setPos(0.0F, 1.0F, -10.0F);
		neck.addChild(spine);
		spine.texOffs(192, 104).addBox(-5.0F, -5.0F, -10.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);
		spine.texOffs(48, 0).addBox(-1.0F, -9.0F, -8.0F, 2.0F, 4.0F, 6.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, 0.0F, -10.0F);
		spine.addChild(head);
		head.texOffs(176, 44).addBox(-6.0F, -2.0F, -30.0F, 12.0F, 5.0F, 16.0F, 0.0F, false);
		head.texOffs(112, 30).addBox(-8.0F, -9.0F, -16.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);
		head.texOffs(0, 0).addBox(3.0F, -13.0F, -10.0F, 2.0F, 4.0F, 6.0F, 0.0F, false);
		head.texOffs(112, 0).addBox(3.0F, -4.0F, -28.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);

		jaw = new ModelRenderer(this);
		jaw.setPos(0.0F, 3.0F, -16.0F);
		head.addChild(jaw);
		jaw.texOffs(176, 65).addBox(-6.0F, 0.0F, -14.0F, 12.0F, 4.0F, 16.0F, 0.0F, false);

		mirrored = new ModelRenderer(this);
		mirrored.setPos(0.0F, 30.0F, 55.0F);
		head.addChild(mirrored);
		mirrored.texOffs(0, 0).addBox(-5.0F, -43.0F, -65.0F, 2.0F, 4.0F, 6.0F, 0.0F, true);
		mirrored.texOffs(112, 0).addBox(-5.0F, -34.0F, -83.0F, 2.0F, 2.0F, 4.0F, 0.0F, true);

		body = new ModelRenderer(this);
		body.setPos(0.0F, 22.0F, 22.0F);
		rotate.addChild(body);
		body.texOffs(7, 5).addBox(-11.0F, -19.0F, -28.0F, 22.0F, 19.0F, 59.0F, 0.0F, false);
		body.texOffs(222, 55).addBox(-1.0F, -23.0F, -17.0F, 2.0F, 4.0F, 10.0F, 0.0F, false);
		body.texOffs(222, 55).addBox(-1.0F, -23.0F, 0.0F, 2.0F, 4.0F, 10.0F, 0.0F, false);
		body.texOffs(222, 55).addBox(-1.0F, -23.0F, 17.0F, 2.0F, 4.0F, 10.0F, 0.0F, false);

		left_wing = new ModelRenderer(this);
		left_wing.setPos(11.0F, -14.0F, -20.0F);
		body.addChild(left_wing);
		left_wing.texOffs(114, 90).addBox(0.0F, -3.0F, -3.0F, 26.0F, 6.0F, 6.0F, 0.0F, true);
		left_wing.texOffs(-56, 200).addBox(0.0F, 0.0F, 2.0F, 26.0F, 0.0F, 56.0F, 0.0F, false);

		left_wing_middle = new ModelRenderer(this);
		left_wing_middle.setPos(26.0F, 0.0F, 0.0F);
		left_wing.addChild(left_wing_middle);
		left_wing_middle.texOffs(-31, 144).addBox(0.0F, 0.0F, 2.0F, 43.0F, 0.0F, 56.0F, 0.0F, false);
		left_wing_middle.texOffs(112, 136).addBox(0.0F, -2.0F, -2.0F, 43.0F, 4.0F, 4.0F, 0.0F, true);

		left_wing_tip = new ModelRenderer(this);
		left_wing_tip.setPos(43.0F, 0.0F, 0.0F);
		left_wing_middle.addChild(left_wing_tip);
		left_wing_tip.texOffs(-31, 88).addBox(0.0F, 0.0F, 2.0F, 43.0F, 0.0F, 56.0F, 0.0F, false);
		left_wing_tip.texOffs(112, 136).addBox(0.0F, -2.0F, -2.0F, 43.0F, 4.0F, 4.0F, 0.0F, true);

		right_wing = new ModelRenderer(this);
		right_wing.setPos(-11.0F, -14.0F, -20.0F);
		body.addChild(right_wing);
		right_wing.texOffs(114, 90).addBox(-26.0F, -3.0F, -3.0F, 26.0F, 6.0F, 6.0F, 0.0F, false);
		right_wing.texOffs(-56, 200).addBox(-26.0F, 0.0F, 2.0F, 26.0F, 0.0F, 56.0F, 0.0F, true);

		right_wing_middle = new ModelRenderer(this);
		right_wing_middle.setPos(-26.0F, 0.0F, 0.0F);
		right_wing.addChild(right_wing_middle);
		right_wing_middle.texOffs(-31, 144).addBox(-43.0F, 0.0F, 2.0F, 43.0F, 0.0F, 56.0F, 0.0F, true);
		right_wing_middle.texOffs(112, 136).addBox(-43.0F, -2.0F, -2.0F, 43.0F, 4.0F, 4.0F, 0.0F, false);

		right_wing_tip = new ModelRenderer(this);
		right_wing_tip.setPos(-43.0F, 0.0F, 0.0F);
		right_wing_middle.addChild(right_wing_tip);
		right_wing_tip.texOffs(-31, 88).addBox(-43.0F, 0.0F, 2.0F, 43.0F, 0.0F, 56.0F, 0.0F, true);
		right_wing_tip.texOffs(112, 136).addBox(-43.0F, -2.0F, -2.0F, 43.0F, 4.0F, 4.0F, 0.0F, false);

		tale = new ModelRenderer(this);
		tale.setPos(0.0F, -7.0F, 31.0F);
		body.addChild(tale);
		tale.texOffs(115, 170).addBox(-7.0F, -6.0F, 0.0F, 14.0F, 12.0F, 20.0F, 0.0F, false);
		tale.texOffs(48, 0).addBox(-1.0F, -10.0F, 2.0F, 2.0F, 4.0F, 6.0F, 0.0F, false);
		tale.texOffs(48, 0).addBox(-1.0F, -10.0F, 12.0F, 2.0F, 4.0F, 6.0F, 0.0F, false);

		spine4 = new ModelRenderer(this);
		spine4.setPos(0.0F, 1.0F, 20.0F);
		tale.addChild(spine4);
		spine4.texOffs(165, 158).addBox(-5.0F, -5.0F, 0.0F, 10.0F, 10.0F, 20.0F, 0.0F, false);
		spine4.texOffs(48, 0).addBox(-1.0F, -9.0F, 2.0F, 2.0F, 4.0F, 6.0F, 0.0F, false);
		spine4.texOffs(48, 0).addBox(-1.0F, -9.0F, 12.0F, 2.0F, 4.0F, 6.0F, 0.0F, false);

		spine5 = new ModelRenderer(this);
		spine5.setPos(0.0F, 1.0F, 20.0F);
		spine4.addChild(spine5);
		spine5.texOffs(165, 190).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 7.0F, 20.0F, 0.0F, false);
		spine5.texOffs(3, 52).addBox(-1.0F, -7.0F, 2.0F, 2.0F, 3.0F, 6.0F, 0.0F, false);
		spine5.texOffs(3, 52).addBox(-1.0F, -7.0F, 12.0F, 2.0F, 3.0F, 6.0F, 0.0F, false);

		spine6 = new ModelRenderer(this);
		spine6.setPos(0.0F, 0.0F, 20.0F);
		spine5.addChild(spine6);
		spine6.texOffs(135, 204).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 20.0F, 0.0F, false);
		spine6.texOffs(3, 52).addBox(-1.0F, -5.0F, 2.0F, 2.0F, 3.0F, 6.0F, 0.0F, false);
		spine6.texOffs(3, 52).addBox(-1.0F, -5.0F, 12.0F, 2.0F, 3.0F, 6.0F, 0.0F, false);

		front_left_leg = new ModelRenderer(this);
		front_left_leg.setPos(8.0F, -5.0F, -20.0F);
		body.addChild(front_left_leg);
		front_left_leg.texOffs(112, 104).addBox(0.0F, -4.0F, -4.0F, 8.0F, 20.0F, 8.0F, 0.0F, false);

		front_left_shin = new ModelRenderer(this);
		front_left_shin.setPos(4.0F, 16.0F, 0.0F);
		front_left_leg.addChild(front_left_shin);
		front_left_shin.texOffs(226, 138).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 20.0F, 6.0F, 0.0F, false);

		front_left_foot = new ModelRenderer(this);
		front_left_foot.setPos(0.0F, 20.0F, 0.0F);
		front_left_shin.addChild(front_left_foot);
		front_left_foot.texOffs(144, 104).addBox(-4.0F, 0.0F, -12.0F, 8.0F, 4.0F, 16.0F, 0.0F, false);

		back_left_leg = new ModelRenderer(this);
		back_left_leg.setPos(11.0F, -6.0F, 19.0F);
		body.addChild(back_left_leg);
		back_left_leg.texOffs(4, 2).addBox(-3.0F, -7.0F, -7.0F, 14.0F, 25.0F, 14.0F, 0.0F, false);

		back_left_shin = new ModelRenderer(this);
		back_left_shin.setPos(4.0F, 18.0F, 0.0F);
		back_left_leg.addChild(back_left_shin);
		back_left_shin.texOffs(200, 2).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 26.0F, 10.0F, 0.0F, false);

		back_left_foot = new ModelRenderer(this);
		back_left_foot.setPos(0.0F, 26.0F, 0.0F);
		back_left_shin.addChild(back_left_foot);
		back_left_foot.texOffs(115, 1).addBox(-8.0F, 0.0F, -16.0F, 16.0F, 6.0F, 23.0F, 0.0F, false);

		front_right_leg = new ModelRenderer(this);
		front_right_leg.setPos(-8.0F, -5.0F, -20.0F);
		body.addChild(front_right_leg);
		front_right_leg.texOffs(112, 104).addBox(-8.0F, -4.0F, -4.0F, 8.0F, 20.0F, 8.0F, 0.0F, false);

		front_right_shin = new ModelRenderer(this);
		front_right_shin.setPos(-4.0F, 16.0F, 0.0F);
		front_right_leg.addChild(front_right_shin);
		front_right_shin.texOffs(226, 138).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 20.0F, 6.0F, 0.0F, false);

		front_right_foot = new ModelRenderer(this);
		front_right_foot.setPos(0.0F, 20.0F, 0.0F);
		front_right_shin.addChild(front_right_foot);
		front_right_foot.texOffs(144, 104).addBox(-4.0F, 0.0F, -12.0F, 8.0F, 4.0F, 16.0F, 0.0F, false);

		back_right_leg = new ModelRenderer(this);
		back_right_leg.setPos(-11.0F, -6.0F, 19.0F);
		body.addChild(back_right_leg);
		back_right_leg.texOffs(4, 2).addBox(-11.0F, -7.0F, -7.0F, 14.0F, 25.0F, 14.0F, 0.0F, false);

		back_right_shin = new ModelRenderer(this);
		back_right_shin.setPos(-4.0F, 18.0F, 0.0F);
		back_right_leg.addChild(back_right_shin);
		back_right_shin.texOffs(200, 2).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 26.0F, 10.0F, 0.0F, false);

		back_right_foot = new ModelRenderer(this);
		back_right_foot.setPos(0.0F, 26.0F, 0.0F);
		back_right_shin.addChild(back_right_foot);
		back_right_foot.texOffs(115, 1).addBox(-8.0F, 0.0F, -16.0F, 16.0F, 6.0F, 23.0F, 0.0F, false);
	}

    @Override
    protected Iterable<ModelRenderer> headParts() {
        return ImmutableList.of(this.young ? this.childNeck : this.neck);
    }

    @Override
    protected Iterable<ModelRenderer> bodyParts() {
        return ImmutableList.of(this.body);
    }

    public class LegAngles {
        public double legAngle;
        public double shinAngle;
        public double footAngle;
    }

    public LegAngles getAngles(T entityIn, boolean kneeInFront, double thighLength, double shinLength, double joint2Center, double radius, double phase, double clampHeight) {
        LegAngles angles = new LegAngles();
        double direction = kneeInFront ? -1.0d : 1.0d;

        //entityIn.updateLastPos();
        Vector3d cycleVec;
        if (!entityIn.isStill) {
            cycleVec = new Vector3d(Math.sin(entityIn.legAnimation + phase) * radius * -direction, Math.cos(entityIn.legAnimation + phase) * radius, 0.0d);
        } else {
            cycleVec = new Vector3d(0.0d, 0.0d, 0.0d);
        }

        Vector3d joint2AnkleVec = new Vector3d(cycleVec.x, joint2Center - cycleVec.y, 0.0d);
        if (joint2AnkleVec.y > clampHeight) {
            joint2AnkleVec = new Vector3d(joint2AnkleVec.x, clampHeight, 0.0d);
        }
        double joint2AnkleLength = joint2AnkleVec.length();

        double joint2AnkleAngel = Math.atan(joint2AnkleVec.x / joint2AnkleVec.y);
        double thighAngle = Math.acos(
                (Math.pow(shinLength, 2) - Math.pow(thighLength, 2) - Math.pow(joint2AnkleLength, 2)) /
                        (-2 * joint2AnkleLength * thighLength)
        );
        angles.legAngle = (joint2AnkleAngel + thighAngle) * direction;
        angles.shinAngle = (Math.acos(
                (Math.pow(joint2AnkleLength, 2) - Math.pow(thighLength, 2) - Math.pow(shinLength, 2)) /
                        (-2 * thighLength * shinLength)
        ) - Math.PI) * direction;

        angles.footAngle = (Math.abs(angles.shinAngle) - Math.abs(angles.legAngle)) * direction;

        return angles;
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //body
        rotate.zRot = (float)(Math.PI/4);

        // head
        if (entityIn.isBaby()) {
            this.head2.xRot = headPitch * ((float) Math.PI / 180F);
            this.head2.yRot = netHeadYaw * ((float) Math.PI / 180F);
        } else {
            headPitch /= 3;
            netHeadYaw /= 3;
            this.head.xRot = headPitch * ((float) Math.PI / 180F);
            this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
            this.neck.xRot = headPitch * ((float) Math.PI / 180F);
            this.neck.yRot = netHeadYaw * ((float) Math.PI / 180F);
            this.spine.xRot = headPitch * ((float) Math.PI / 180F);
            this.spine.yRot = netHeadYaw * ((float) Math.PI / 180F);
        }

        //legs
        //front
        LegAngles front_right_angles = getAngles(entityIn, false, 1.0d, 1.25d, 1.625d, 0.625d, 0.0d, 1.5625d);
        this.front_right_leg.xRot = (float) front_right_angles.legAngle;
        this.front_right_shin.xRot = (float) front_right_angles.shinAngle;
        this.front_right_foot.xRot = (float) front_right_angles.footAngle;

        LegAngles front_left_angles = getAngles(entityIn, false, 1.0d, 1.25d, 1.625d, 0.625d, Math.PI, 1.5625d);
        this.front_left_leg.xRot = (float) front_left_angles.legAngle;
        this.front_left_shin.xRot = (float) front_left_angles.shinAngle;
        this.front_left_foot.xRot = (float) front_left_angles.footAngle;

        //back
        LegAngles back_right_angles = getAngles(entityIn, true, 1.5625d, 1.625d, 1.625d, 0.8d, 0.0d, 1.625d);
        this.back_right_leg.xRot = (float) back_right_angles.legAngle;
        this.back_right_shin.xRot = (float) back_right_angles.shinAngle;
        this.back_right_foot.xRot = (float) back_right_angles.footAngle;

        LegAngles back_left_angles = getAngles(entityIn, true, 1.5625d, 1.625d, 1.625d, 0.8d, Math.PI, 1.625d);
        this.back_left_leg.xRot = (float) back_left_angles.legAngle;
        this.back_left_shin.xRot = (float) back_left_angles.shinAngle;
        this.back_left_foot.xRot = (float) back_left_angles.footAngle;

        //wings
        //right
        this.right_wing.yRot = 0.2617f;
        this.right_wing.zRot = -0.4712f;

        this.right_wing_middle.xRot = 0.1309f;
        this.right_wing_middle.yRot = 0.3491f;
        this.right_wing_middle.zRot = 2.6616f;

        this.right_wing_tip.zRot = -2.8798f;

        //left
        this.left_wing.yRot = -0.2617f;
        this.left_wing.zRot = 0.4712f;

        this.left_wing_middle.xRot = 0.1309f;
        this.left_wing_middle.yRot = -0.3491f;
        this.left_wing_middle.zRot = -2.6616f;

        this.left_wing_tip.zRot = 2.8798f;
    }
}