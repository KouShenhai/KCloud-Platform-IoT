/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.common.mybatisplus.config;

import com.baomidou.mybatisplus.extension.parser.cache.AbstractCaffeineJsqlParseCache;
import com.github.benmanes.caffeine.cache.Cache;
import org.jspecify.annotations.NonNull;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.fory.constant.ForyConstants;

/**
 * jsqlparser 缓存 fory 序列化 Caffeine 缓存实现.
 *
 * @author laokou
 */
public class ForySerialCaffeineJsqlParseCache extends AbstractCaffeineJsqlParseCache {

	static {
		ForyFactory.INSTANCE.registerSerializer(net.sf.jsqlparser.expression.Function.HavingClause.class,
				HavingClauseSerializer.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Alias.class, ForyConstants.C_1);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Alias.AliasColumn.class, ForyConstants.C_2);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.AllValue.class, ForyConstants.C_3);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.AnalyticExpression.class, ForyConstants.C_4);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.AnyComparisonExpression.class, ForyConstants.C_5);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ArrayConstructor.class, ForyConstants.C_6);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ArrayExpression.class, ForyConstants.C_7);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.CaseExpression.class, ForyConstants.C_8);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.CastExpression.class, ForyConstants.C_9);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.CollateExpression.class, ForyConstants.C_10);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ConnectByRootOperator.class, ForyConstants.C_11);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.DateTimeLiteralExpression.class, ForyConstants.C_12);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.DateValue.class, ForyConstants.C_13);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.DoubleValue.class, ForyConstants.C_14);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ExtractExpression.class, ForyConstants.C_15);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.FilterOverImpl.class, ForyConstants.C_16);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Function.class, ForyConstants.C_17);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Function.HavingClause.class, ForyConstants.C_18);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.HexValue.class, ForyConstants.C_19);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.IntervalExpression.class, ForyConstants.C_20);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JdbcNamedParameter.class, ForyConstants.C_21);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JdbcParameter.class, ForyConstants.C_22);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonAggregateFunction.class, ForyConstants.C_23);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonExpression.class, ForyConstants.C_24);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonFunction.class, ForyConstants.C_25);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonFunctionExpression.class, ForyConstants.C_26);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonKeyValuePair.class, ForyConstants.C_27);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.KeepExpression.class, ForyConstants.C_28);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.LongValue.class, ForyConstants.C_29);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.MySQLGroupConcat.class, ForyConstants.C_30);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.MySQLIndexHint.class, ForyConstants.C_31);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NextValExpression.class, ForyConstants.C_32);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NotExpression.class, ForyConstants.C_33);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NullValue.class, ForyConstants.C_34);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NumericBind.class, ForyConstants.C_35);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleHierarchicalExpression.class,
				ForyConstants.C_36);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleHint.class, ForyConstants.C_37);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleNamedFunctionParameter.class,
				ForyConstants.C_38);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OrderByClause.class, ForyConstants.C_39);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OverlapsCondition.class, ForyConstants.C_40);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.PartitionByClause.class, ForyConstants.C_41);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.RangeExpression.class, ForyConstants.C_42);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.RowConstructor.class, ForyConstants.C_43);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.RowGetExpression.class, ForyConstants.C_44);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.SQLServerHints.class, ForyConstants.C_45);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.SignedExpression.class, ForyConstants.C_46);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.StringValue.class, ForyConstants.C_47);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimeKeyExpression.class, ForyConstants.C_48);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimeValue.class, ForyConstants.C_49);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimestampValue.class, ForyConstants.C_50);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimezoneExpression.class, ForyConstants.C_51);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TranscodingFunction.class, ForyConstants.C_52);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TrimFunction.class, ForyConstants.C_53);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.UserVariable.class, ForyConstants.C_54);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.VariableAssignment.class, ForyConstants.C_55);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WhenClause.class, ForyConstants.C_56);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowDefinition.class, ForyConstants.C_57);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowElement.class, ForyConstants.C_58);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowOffset.class, ForyConstants.C_59);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowRange.class, ForyConstants.C_60);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.XMLSerializeExpr.class, ForyConstants.C_61);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Addition.class,
				ForyConstants.C_62);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd.class,
				ForyConstants.C_63);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift.class,
				ForyConstants.C_64);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr.class,
				ForyConstants.C_65);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift.class,
				ForyConstants.C_66);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor.class,
				ForyConstants.C_67);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Concat.class,
				ForyConstants.C_68);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Division.class,
				ForyConstants.C_69);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.IntegerDivision.class,
				ForyConstants.C_70);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Modulo.class,
				ForyConstants.C_71);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Multiplication.class,
				ForyConstants.C_72);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Subtraction.class,
				ForyConstants.C_73);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.AndExpression.class,
				ForyConstants.C_74);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.OrExpression.class,
				ForyConstants.C_75);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.XorExpression.class,
				ForyConstants.C_76);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Between.class,
				ForyConstants.C_77);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ContainedBy.class,
				ForyConstants.C_78);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Contains.class,
				ForyConstants.C_79);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.DoubleAnd.class,
				ForyConstants.C_80);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.EqualsTo.class,
				ForyConstants.C_81);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ExistsExpression.class,
				ForyConstants.C_82);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ExpressionList.class,
				ForyConstants.C_83);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.FullTextSearch.class,
				ForyConstants.C_84);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GeometryDistance.class,
				ForyConstants.C_85);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GreaterThan.class,
				ForyConstants.C_86);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals.class,
				ForyConstants.C_87);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.InExpression.class,
				ForyConstants.C_88);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsBooleanExpression.class,
				ForyConstants.C_89);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsDistinctExpression.class,
				ForyConstants.C_90);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsNullExpression.class,
				ForyConstants.C_91);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.JsonOperator.class,
				ForyConstants.C_92);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.LikeExpression.class,
				ForyConstants.C_93);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Matches.class,
				ForyConstants.C_94);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MemberOfExpression.class,
				ForyConstants.C_95);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MinorThan.class,
				ForyConstants.C_96);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MinorThanEquals.class,
				ForyConstants.C_97);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.NamedExpressionList.class,
				ForyConstants.C_98);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.NotEqualsTo.class,
				ForyConstants.C_99);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList.class,
				ForyConstants.C_100);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator.class,
				ForyConstants.C_101);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.SimilarToExpression.class,
				ForyConstants.C_102);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.TSQLLeftJoin.class,
				ForyConstants.C_103);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.TSQLRightJoin.class,
				ForyConstants.C_104);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.parser.ASTNodeAccessImpl.class, ForyConstants.C_105);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.parser.Token.class, ForyConstants.C_106);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Column.class, ForyConstants.C_107);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Sequence.class, ForyConstants.C_108);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Synonym.class, ForyConstants.C_109);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Table.class, ForyConstants.C_110);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.Block.class, ForyConstants.C_111);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.Commit.class, ForyConstants.C_112);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.DeclareStatement.class, ForyConstants.C_113);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.DeclareStatement.TypeDefExpr.class,
				ForyConstants.C_114);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.DescribeStatement.class, ForyConstants.C_115);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ExplainStatement.class, ForyConstants.C_116);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ExplainStatement.Option.class, ForyConstants.C_117);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.IfElseStatement.class, ForyConstants.C_118);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.OutputClause.class, ForyConstants.C_119);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.PurgeStatement.class, ForyConstants.C_120);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ReferentialAction.class, ForyConstants.C_121);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ResetStatement.class, ForyConstants.C_122);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.RollbackStatement.class, ForyConstants.C_123);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.SavepointStatement.class, ForyConstants.C_124);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.SetStatement.class, ForyConstants.C_125);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ShowColumnsStatement.class, ForyConstants.C_126);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ShowStatement.class, ForyConstants.C_127);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.Statements.class, ForyConstants.C_128);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.UnsupportedStatement.class, ForyConstants.C_129);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.UseStatement.class, ForyConstants.C_130);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.Alter.class, ForyConstants.C_131);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.class, ForyConstants.C_132);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDataType.class,
				ForyConstants.C_133);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDropDefault.class,
				ForyConstants.C_134);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDropNotNull.class,
				ForyConstants.C_135);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterSession.class, ForyConstants.C_136);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterSystemStatement.class,
				ForyConstants.C_137);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.RenameTableStatement.class,
				ForyConstants.C_138);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.sequence.AlterSequence.class,
				ForyConstants.C_139);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.analyze.Analyze.class, ForyConstants.C_140);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.comment.Comment.class, ForyConstants.C_141);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.function.CreateFunction.class,
				ForyConstants.C_142);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.index.CreateIndex.class, ForyConstants.C_143);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.procedure.CreateProcedure.class,
				ForyConstants.C_144);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.schema.CreateSchema.class,
				ForyConstants.C_145);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.sequence.CreateSequence.class,
				ForyConstants.C_146);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.synonym.CreateSynonym.class,
				ForyConstants.C_147);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.CheckConstraint.class,
				ForyConstants.C_148);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ColDataType.class, ForyConstants.C_149);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ColumnDefinition.class,
				ForyConstants.C_150);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.CreateTable.class, ForyConstants.C_151);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ExcludeConstraint.class,
				ForyConstants.C_152);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ForeignKeyIndex.class,
				ForyConstants.C_153);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.Index.class, ForyConstants.C_154);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.Index.ColumnParams.class,
				ForyConstants.C_155);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.NamedConstraint.class,
				ForyConstants.C_156);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.RowMovement.class, ForyConstants.C_157);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.view.AlterView.class, ForyConstants.C_158);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.view.CreateView.class, ForyConstants.C_159);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.delete.Delete.class, ForyConstants.C_160);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.drop.Drop.class, ForyConstants.C_161);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.execute.Execute.class, ForyConstants.C_162);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.grant.Grant.class, ForyConstants.C_163);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.Insert.class, ForyConstants.C_164);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertConflictAction.class,
				ForyConstants.C_165);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertConflictTarget.class,
				ForyConstants.C_166);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.Merge.class, ForyConstants.C_167);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeDelete.class, ForyConstants.C_168);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeInsert.class, ForyConstants.C_169);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeUpdate.class, ForyConstants.C_170);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.refresh.RefreshMaterializedViewStatement.class,
				ForyConstants.C_171);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.AllColumns.class, ForyConstants.C_172);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.AllTableColumns.class, ForyConstants.C_173);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Distinct.class, ForyConstants.C_174);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ExceptOp.class, ForyConstants.C_175);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Fetch.class, ForyConstants.C_176);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.First.class, ForyConstants.C_177);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForClause.class, ForyConstants.C_178);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.GroupByElement.class, ForyConstants.C_179);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.IntersectOp.class, ForyConstants.C_180);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Join.class, ForyConstants.C_181);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLJoinWindow.class, ForyConstants.C_182);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLWindow.class, ForyConstants.C_183);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.LateralSubSelect.class, ForyConstants.C_184);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.LateralView.class, ForyConstants.C_185);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Limit.class, ForyConstants.C_186);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.MinusOp.class, ForyConstants.C_187);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Offset.class, ForyConstants.C_188);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OptimizeFor.class, ForyConstants.C_189);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OrderByElement.class, ForyConstants.C_190);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ParenthesedFromItem.class,
				ForyConstants.C_191);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ParenthesedSelect.class, ForyConstants.C_192);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Pivot.class, ForyConstants.C_193);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PivotXml.class, ForyConstants.C_194);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PlainSelect.class, ForyConstants.C_195);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SelectItem.class, ForyConstants.C_196);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperationList.class, ForyConstants.C_197);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperationList.SetOperationType.class,
				ForyConstants.C_198);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Skip.class, ForyConstants.C_199);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.TableFunction.class, ForyConstants.C_200);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.TableStatement.class, ForyConstants.C_201);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Top.class, ForyConstants.C_202);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.UnPivot.class, ForyConstants.C_203);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.UnionOp.class, ForyConstants.C_204);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Values.class, ForyConstants.C_205);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Wait.class, ForyConstants.C_206);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.WithIsolation.class, ForyConstants.C_207);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.WithItem.class, ForyConstants.C_208);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.show.ShowIndexStatement.class, ForyConstants.C_209);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.show.ShowTablesStatement.class, ForyConstants.C_210);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.truncate.Truncate.class, ForyConstants.C_211);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.Update.class, ForyConstants.C_212);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.UpdateSet.class, ForyConstants.C_213);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.upsert.Upsert.class, ForyConstants.C_214);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultiAndExpression.class,
				ForyConstants.C_215);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultiOrExpression.class,
				ForyConstants.C_216);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.BinaryExpression.class, ForyConstants.C_217);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ComparisonOperator.class,
				ForyConstants.C_218);
		ForyFactory.INSTANCE.register(
				net.sf.jsqlparser.expression.operators.relational.OldOracleJoinBinaryExpression.class,
				ForyConstants.C_219);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Function.NullHandling.class, ForyConstants.C_220);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.CreateFunctionalStatement.class, ForyConstants.C_221);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Select.class, ForyConstants.C_222);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperation.class, ForyConstants.C_223);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultipleExpression.class,
				ForyConstants.C_224);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertModifierPriority.class,
				ForyConstants.C_225);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OrderByElement.NullOrdering.class,
				ForyConstants.C_226);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForMode.class, ForyConstants.C_227);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.MySqlSqlCacheFlags.class, ForyConstants.C_228);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PlainSelect.BigQuerySelectQualifier.class,
				ForyConstants.C_229);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.UpdateModifierPriority.class,
				ForyConstants.C_230);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.LikeExpression.KeyWord.class,
				ForyConstants.C_231);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.delete.DeleteModifierPriority.class,
				ForyConstants.C_232);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Partition.class, ForyConstants.C_233);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.ConflictActionType.class, ForyConstants.C_234);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForClause.ForOption.class,
				ForyConstants.C_235);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLWindow.TimeUnit.class,
				ForyConstants.C_236);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.First.Keyword.class, ForyConstants.C_237);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowElement.Type.class, ForyConstants.C_238);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowOffset.Type.class, ForyConstants.C_239);
	}

	public ForySerialCaffeineJsqlParseCache(Cache<@NonNull String, byte[]> cache) {
		super(cache);
	}

	@Override
	public byte[] serialize(Object obj) {
		return ForyFactory.INSTANCE.serialize(obj);
	}

	@Override
	public Object deserialize(String sql, byte[] bytes) {
		return ForyFactory.INSTANCE.deserialize(bytes);
	}

}
