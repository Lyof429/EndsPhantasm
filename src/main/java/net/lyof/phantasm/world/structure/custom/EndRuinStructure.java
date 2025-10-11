package net.lyof.phantasm.world.structure.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lyof.phantasm.world.structure.ModStructures;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.OceanRuinStructure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.List;

public class EndRuinStructure extends OceanRuinStructure {
    public static final Codec<EndRuinStructure> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(configCodecBuilder(instance),
                    Identifier.CODEC.listOf().fieldOf("pieces").forGetter(structure -> structure.pieces)
            ).apply(instance, EndRuinStructure::new));


    public final List<Identifier> pieces;

    public EndRuinStructure(Config config, List<Identifier> pieces) {
        super(config, BiomeTemperature.WARM, 0.8f, 1);
        this.pieces = pieces;
    }

    @Override
    public StructureType<?> getType() {
        return ModStructures.END_RUIN;
    }

/*
    private void addPieces(StructurePiecesCollector collector, Structure.Context context) {
        BlockPos blockPos = new BlockPos(context.chunkPos().getStartX(), 90, context.chunkPos().getStartZ());
        BlockRotation blockRotation = BlockRotation.random(context.random());
        Generator.addPieces(context.structureTemplateManager(), blockPos, blockRotation, collector, context.random(), this);
    }


    public static class Generator {
        public static final Identifier[] PIECES = new Identifier[]{
                Phantasm.makeID("acidburnt_ruin/sulphurs_1"),
                Phantasm.makeID("acidburnt_ruin/sulphurs_2"),
                Phantasm.makeID("acidburnt_ruin/sulphurs_3")};

        protected static List<BlockPos> getRoomPositions(Random random, BlockPos pos) {
            List<BlockPos> list = new ArrayList<>();
            list.add(pos.add(-16 + MathHelper.nextInt(random, 1, 8), 0, 16 + MathHelper.nextInt(random, 1, 7)));
            list.add(pos.add(-16 + MathHelper.nextInt(random, 1, 8), 0, MathHelper.nextInt(random, 1, 7)));
            list.add(pos.add(-16 + MathHelper.nextInt(random, 1, 8), 0, -16 + MathHelper.nextInt(random, 4, 8)));
            list.add(pos.add(MathHelper.nextInt(random, 1, 7), 0, 16 + MathHelper.nextInt(random, 1, 7)));
            list.add(pos.add(MathHelper.nextInt(random, 1, 7), 0, -16 + MathHelper.nextInt(random, 4, 6)));
            list.add(pos.add(16 + MathHelper.nextInt(random, 1, 7), 0, 16 + MathHelper.nextInt(random, 3, 8)));
            list.add(pos.add(16 + MathHelper.nextInt(random, 1, 7), 0, MathHelper.nextInt(random, 1, 7)));
            list.add(pos.add(16 + MathHelper.nextInt(random, 1, 7), 0, -16 + MathHelper.nextInt(random, 4, 8)));
            return list;
        }

        protected static void addPieces(StructureTemplateManager manager, BlockPos pos, BlockRotation rotation, StructurePiecesHolder holder,
                                     Random random, EndRuinStructure structure, boolean large, float integrity) {
            for (int i = 0; i < random.nextBetween(2, 5); i++)
                holder.addPiece(new OceanRuinGenerator.Piece(manager, Util.getRandom(PIECES, random), pos, rotation, -0.95f, structure.biomeTemperature, large));
        }

        public static void addPieces(StructureTemplateManager manager, BlockPos pos, BlockRotation rotation, StructurePiecesHolder holder, 
                                     Random random, EndRuinStructure structure) {
            boolean bl = random.nextFloat() <= 0.8;
            float f = bl ? 0.9F : 0.8F;
            addPieces(manager, pos, rotation, holder, random, structure, bl, f);
            addCluster(manager, random, rotation, pos, structure, holder);
        }

        protected static void addCluster(StructureTemplateManager manager, Random random, BlockRotation rotation, BlockPos pos, EndRuinStructure structure, StructurePiecesHolder pieces) {
            BlockPos blockPos = new BlockPos(pos.getX(), 90, pos.getZ());
            BlockPos blockPos2 = StructureTemplate.transformAround(new BlockPos(15, 0, 15), BlockMirror.NONE, rotation, BlockPos.ORIGIN).add(blockPos);
            BlockBox blockBox = BlockBox.create(blockPos, blockPos2);
            BlockPos blockPos3 = new BlockPos(Math.min(blockPos.getX(), blockPos2.getX()), blockPos.getY(), Math.min(blockPos.getZ(), blockPos2.getZ()));
            List<BlockPos> list = getRoomPositions(random, blockPos3);
            int i = MathHelper.nextInt(random, 4, 8);

            for (int j = 0; j < i; ++j) {
                if (!list.isEmpty()) {
                    int k = random.nextInt(list.size());
                    BlockPos blockPos4 = (BlockPos)list.remove(k);
                    BlockRotation blockRotation = BlockRotation.random(random);
                    BlockPos blockPos5 = StructureTemplate.transformAround(new BlockPos(5, 0, 6), BlockMirror.NONE, blockRotation, BlockPos.ORIGIN).add(blockPos4);
                    BlockBox blockBox2 = BlockBox.create(blockPos4, blockPos5);
                    if (!blockBox2.intersects(blockBox)) {
                        addPieces(manager, blockPos4, blockRotation, pieces, random, structure, false, 0.8F);
                    }
                }
            }
        }
    }


    public static class Piece extends SimpleStructurePiece {
        private final float integrity;
        private final boolean large;

        public Piece(StructureTemplateManager structureTemplateManager, Identifier template, BlockPos pos, BlockRotation rotation, float integrity, EndRuinStructure.BiomeTemperature biomeType, boolean large) {
            super(StructurePieceType.OCEAN_TEMPLE, 0, structureTemplateManager, template, template.toString(), createPlacementData(rotation, integrity, biomeType), pos);
            this.integrity = integrity;
            this.large = large;
        }

        private Piece(StructureTemplateManager holder, NbtCompound nbt, BlockRotation rotation, float integrity, EndRuinStructure.BiomeTemperature biomeType, boolean large) {
            super(StructurePieceType.OCEAN_TEMPLE, nbt, holder, (identifier) -> createPlacementData(rotation, integrity, biomeType));
            this.integrity = integrity;
            this.large = large;
        }

        private static StructurePlacementData createPlacementData(BlockRotation rotation, float integrity, EndRuinStructure.BiomeTemperature temperature) {
            StructureProcessor structureProcessor = temperature == EndRuinStructure.BiomeTemperature.COLD ? OceanRuinGenerator.SUSPICIOUS_GRAVEL_PROCESSOR : OceanRuinGenerator.SUSPICIOUS_SAND_PROCESSOR;
            return (new StructurePlacementData()).setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(new BlockRotStructureProcessor(integrity)).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS).addProcessor(structureProcessor);
        }

        public static OceanRuinGenerator.Piece fromNbt(StructureTemplateManager structureTemplateManager, NbtCompound nbt) {
            BlockRotation blockRotation = BlockRotation.valueOf(nbt.getString("Rot"));
            float f = nbt.getFloat("Integrity");
            EndRuinStructure.BiomeTemperature biomeTemperature = EndRuinStructure.BiomeTemperature.valueOf(nbt.getString("BiomeType"));
            boolean bl = nbt.getBoolean("IsLarge");
            return new OceanRuinGenerator.Piece(structureTemplateManager, nbt, blockRotation, f, biomeTemperature, bl);
        }

        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putString("Rot", this.placementData.getRotation().name());
            nbt.putFloat("Integrity", this.integrity);
            nbt.putString("BiomeType", this.biomeType.toString());
            nbt.putBoolean("IsLarge", this.large);
        }

        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {

        }

        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            int i = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, this.pos.getX(), this.pos.getZ());
            this.pos = new BlockPos(this.pos.getX(), i, this.pos.getZ());
            BlockPos blockPos = StructureTemplate.transformAround(new BlockPos(this.template.getSize().getX() - 1, 0, this.template.getSize().getZ() - 1), BlockMirror.NONE, this.placementData.getRotation(), BlockPos.ORIGIN).add(this.pos);
            this.pos = new BlockPos(this.pos.getX(), this.getGenerationY(this.pos, world, blockPos), this.pos.getZ());
            super.generate(world, structureAccessor, chunkGenerator, random, chunkBox, chunkPos, pivot);
        }

        private int getGenerationY(BlockPos start, BlockView world, BlockPos end) {
            int i = start.getY();
            int j = 512;
            int k = i - 1;
            int l = 0;
            Iterator var8 = BlockPos.iterate(start, end).iterator();

            while(var8.hasNext()) {
                BlockPos blockPos = (BlockPos)var8.next();
                int m = blockPos.getX();
                int n = blockPos.getZ();
                int o = start.getY() - 1;
                BlockPos.Mutable mutable = new BlockPos.Mutable(m, o, n);
                BlockState blockState = world.getBlockState(mutable);

                for(FluidState fluidState = world.getFluidState(mutable); (blockState.isAir() || fluidState.isIn(FluidTags.WATER) || blockState.isIn(BlockTags.ICE)) && o > world.getBottomY() + 1; fluidState = world.getFluidState(mutable)) {
                    --o;
                    mutable.set(m, o, n);
                    blockState = world.getBlockState(mutable);
                }

                j = Math.min(j, o);
                if (o < k - 2) {
                    ++l;
                }
            }

            int p = Math.abs(start.getX() - end.getX());
            if (k - j > 2 && l > p - 2) {
                i = j + 1;
            }

            return i;
        }
    }*/
}
