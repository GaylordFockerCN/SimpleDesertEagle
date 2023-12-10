// Made with Blockbench 4.9.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelbullet_Converted<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "bullet_converted"), "main");
	private final ModelPart group;

	public Modelbullet_Converted(ModelPart root) {
		this.group = root.getChild("group");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition group = partdefinition.addOrReplaceChild("group", CubeListBuilder.create().texOffs(1, 1)
				.addBox(-9.5F, -1.5F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(1, 3)
				.addBox(-9.5F, 1.0F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 3)
				.addBox(-8.0F, -2.0F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 5)
				.addBox(-7.0F, 1.0F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 0).mirror()
				.addBox(-8.5F, -2.0F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(5, 2)
				.addBox(-9.0F, -2.0F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 6)
				.addBox(-6.5F, -1.0F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 7)
				.addBox(-6.5F, -0.5F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 3).mirror()
				.addBox(-6.5F, 0.0F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(8, 5)
				.addBox(-6.5F, 0.5F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(8, 7)
				.addBox(-7.5F, 1.5F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 8)
				.addBox(-8.0F, 1.5F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 9)
				.addBox(-8.5F, 1.5F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(9, 1)
				.addBox(-10.0F, -0.5F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 10)
				.addBox(-10.0F, 0.5F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(8, 11)
				.addBox(-10.0F, 0.0F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 11)
				.addBox(-7.0F, -1.5F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 3).mirror()
				.addBox(-9.0F, 1.5F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(12, 5)
				.addBox(-10.0F, -1.0F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(9, 9)
				.addBox(-7.5F, -2.0F, 4.0F, 0.5F, 0.5F, 8.0F, new CubeDeformation(0.0F)).texOffs(12, 12)
				.addBox(-9.25F, -1.25F, 3.5F, 2.5F, 2.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(6, 12)
				.addBox(-9.125F, -1.0F, 3.25F, 2.25F, 2.25F, 0.5F, new CubeDeformation(0.0F)).texOffs(12, 12)
				.addBox(-9.025F, -0.875F, 3.0F, 2.125F, 2.125F, 0.5F, new CubeDeformation(0.0F)).texOffs(9, 12)
				.addBox(-9.0F, -1.75F, 3.75F, 2.0F, 3.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(1, 14).mirror()
				.addBox(-9.5F, -1.25F, 3.75F, 0.5F, 2.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false).texOffs(3, 14)
				.mirror().addBox(-7.0F, -1.25F, 3.75F, 0.5F, 2.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(2, 8).mirror().addBox(-9.0F, 1.5F, 3.75F, 2.0F, 0.25F, 0.25F, new CubeDeformation(0.0F))
				.mirror(false).texOffs(2, 13).mirror()
				.addBox(-9.0F, -1.5F, 3.75F, 2.0F, 0.25F, 0.25F, new CubeDeformation(0.0F)).mirror(false).texOffs(1, 3)
				.addBox(-9.0F, -1.5F, 11.75F, 2.0F, 3.0F, 0.5F, new CubeDeformation(0.0F)).texOffs(0, 2)
				.addBox(-9.0F, -1.5F, 11.75F, 2.0F, 3.0F, 0.5F, new CubeDeformation(0.0F)).texOffs(0, 2)
				.addBox(-9.5F, -1.0F, 11.75F, 0.5F, 2.0F, 0.5F, new CubeDeformation(0.0F)).texOffs(0, 2)
				.addBox(-7.0F, -1.0F, 11.75F, 0.5F, 2.0F, 0.5F, new CubeDeformation(0.0F)),
				PartPose.offset(8.0F, 24.0F, -8.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		group.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}