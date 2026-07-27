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

/**
 * jsqlparser 缓存 fory 序列化 Caffeine 缓存实现.
 *
 * @author laokou
 */
public class ForySerialCaffeineJsqlParseCache extends AbstractCaffeineJsqlParseCache {

	static {
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Alias.class, ForyFactory.C_1);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Alias.AliasColumn.class, ForyFactory.C_2);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.AllValue.class, ForyFactory.C_3);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.AnalyticExpression.class, ForyFactory.C_4);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.AnyComparisonExpression.class, ForyFactory.C_5);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ArrayConstructor.class, ForyFactory.C_6);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ArrayExpression.class, ForyFactory.C_7);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.CaseExpression.class, ForyFactory.C_8);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.CastExpression.class, ForyFactory.C_9);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.CollateExpression.class, ForyFactory.C_10);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ConnectByRootOperator.class, ForyFactory.C_11);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.DateTimeLiteralExpression.class, ForyFactory.C_12);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.DateValue.class, ForyFactory.C_13);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.DoubleValue.class, ForyFactory.C_14);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ExtractExpression.class, ForyFactory.C_15);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.FilterOverImpl.class, ForyFactory.C_16);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Function.class, ForyFactory.C_17);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Function.HavingClause.class, ForyFactory.C_18);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.HexValue.class, ForyFactory.C_19);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.IntervalExpression.class, ForyFactory.C_20);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JdbcNamedParameter.class, ForyFactory.C_21);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JdbcParameter.class, ForyFactory.C_22);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonAggregateFunction.class, ForyFactory.C_23);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonExpression.class, ForyFactory.C_24);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonFunction.class, ForyFactory.C_25);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonFunctionExpression.class, ForyFactory.C_26);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonKeyValuePair.class, ForyFactory.C_27);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.KeepExpression.class, ForyFactory.C_28);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.LongValue.class, ForyFactory.C_29);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.MySQLGroupConcat.class, ForyFactory.C_30);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.MySQLIndexHint.class, ForyFactory.C_31);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NextValExpression.class, ForyFactory.C_32);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NotExpression.class, ForyFactory.C_33);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NullValue.class, ForyFactory.C_34);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NumericBind.class, ForyFactory.C_35);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleHierarchicalExpression.class,
				ForyFactory.C_36);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleHint.class, ForyFactory.C_37);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleNamedFunctionParameter.class,
				ForyFactory.C_38);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OrderByClause.class, ForyFactory.C_39);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OverlapsCondition.class, ForyFactory.C_40);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.PartitionByClause.class, ForyFactory.C_41);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.RangeExpression.class, ForyFactory.C_42);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.RowConstructor.class, ForyFactory.C_43);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.RowGetExpression.class, ForyFactory.C_44);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.SQLServerHints.class, ForyFactory.C_45);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.SignedExpression.class, ForyFactory.C_46);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.StringValue.class, ForyFactory.C_47);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimeKeyExpression.class, ForyFactory.C_48);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimeValue.class, ForyFactory.C_49);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimestampValue.class, ForyFactory.C_50);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimezoneExpression.class, ForyFactory.C_51);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TranscodingFunction.class, ForyFactory.C_52);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TrimFunction.class, ForyFactory.C_53);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.UserVariable.class, ForyFactory.C_54);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.VariableAssignment.class, ForyFactory.C_55);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WhenClause.class, ForyFactory.C_56);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowDefinition.class, ForyFactory.C_57);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowElement.class, ForyFactory.C_58);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowOffset.class, ForyFactory.C_59);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowRange.class, ForyFactory.C_60);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.XMLSerializeExpr.class, ForyFactory.C_61);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Addition.class,
				ForyFactory.C_62);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd.class,
				ForyFactory.C_63);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift.class,
				ForyFactory.C_64);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr.class,
				ForyFactory.C_65);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift.class,
				ForyFactory.C_66);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor.class,
				ForyFactory.C_67);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Concat.class, ForyFactory.C_68);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Division.class,
				ForyFactory.C_69);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.IntegerDivision.class,
				ForyFactory.C_70);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Modulo.class, ForyFactory.C_71);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Multiplication.class,
				ForyFactory.C_72);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Subtraction.class,
				ForyFactory.C_73);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.AndExpression.class,
				ForyFactory.C_74);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.OrExpression.class,
				ForyFactory.C_75);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.XorExpression.class,
				ForyFactory.C_76);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Between.class,
				ForyFactory.C_77);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ContainedBy.class,
				ForyFactory.C_78);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Contains.class,
				ForyFactory.C_79);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.DoubleAnd.class,
				ForyFactory.C_80);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.EqualsTo.class,
				ForyFactory.C_81);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ExistsExpression.class,
				ForyFactory.C_82);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ExpressionList.class,
				ForyFactory.C_83);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.FullTextSearch.class,
				ForyFactory.C_84);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GeometryDistance.class,
				ForyFactory.C_85);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GreaterThan.class,
				ForyFactory.C_86);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals.class,
				ForyFactory.C_87);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.InExpression.class,
				ForyFactory.C_88);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsBooleanExpression.class,
				ForyFactory.C_89);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsDistinctExpression.class,
				ForyFactory.C_90);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsNullExpression.class,
				ForyFactory.C_91);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.JsonOperator.class,
				ForyFactory.C_92);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.LikeExpression.class,
				ForyFactory.C_93);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Matches.class,
				ForyFactory.C_94);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MemberOfExpression.class,
				ForyFactory.C_95);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MinorThan.class,
				ForyFactory.C_96);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MinorThanEquals.class,
				ForyFactory.C_97);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.NamedExpressionList.class,
				ForyFactory.C_98);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.NotEqualsTo.class,
				ForyFactory.C_99);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList.class,
				ForyFactory.C_100);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator.class,
				ForyFactory.C_101);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.SimilarToExpression.class,
				ForyFactory.C_102);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.TSQLLeftJoin.class,
				ForyFactory.C_103);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.TSQLRightJoin.class,
				ForyFactory.C_104);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.parser.ASTNodeAccessImpl.class, ForyFactory.C_105);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.parser.Token.class, ForyFactory.C_106);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Column.class, ForyFactory.C_107);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Sequence.class, ForyFactory.C_108);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Synonym.class, ForyFactory.C_109);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Table.class, ForyFactory.C_110);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.Block.class, ForyFactory.C_111);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.Commit.class, ForyFactory.C_112);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.DeclareStatement.class, ForyFactory.C_113);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.DeclareStatement.TypeDefExpr.class,
				ForyFactory.C_114);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.DescribeStatement.class, ForyFactory.C_115);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ExplainStatement.class, ForyFactory.C_116);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ExplainStatement.Option.class, ForyFactory.C_117);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.IfElseStatement.class, ForyFactory.C_118);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.OutputClause.class, ForyFactory.C_119);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.PurgeStatement.class, ForyFactory.C_120);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ReferentialAction.class, ForyFactory.C_121);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ResetStatement.class, ForyFactory.C_122);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.RollbackStatement.class, ForyFactory.C_123);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.SavepointStatement.class, ForyFactory.C_124);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.SetStatement.class, ForyFactory.C_125);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ShowColumnsStatement.class, ForyFactory.C_126);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ShowStatement.class, ForyFactory.C_127);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.Statements.class, ForyFactory.C_128);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.UnsupportedStatement.class, ForyFactory.C_129);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.UseStatement.class, ForyFactory.C_130);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.Alter.class, ForyFactory.C_131);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.class, ForyFactory.C_132);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDataType.class,
				ForyFactory.C_133);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDropDefault.class,
				ForyFactory.C_134);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDropNotNull.class,
				ForyFactory.C_135);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterSession.class, ForyFactory.C_136);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterSystemStatement.class, ForyFactory.C_137);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.RenameTableStatement.class, ForyFactory.C_138);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.sequence.AlterSequence.class,
				ForyFactory.C_139);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.analyze.Analyze.class, ForyFactory.C_140);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.comment.Comment.class, ForyFactory.C_141);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.function.CreateFunction.class,
				ForyFactory.C_142);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.index.CreateIndex.class, ForyFactory.C_143);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.procedure.CreateProcedure.class,
				ForyFactory.C_144);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.schema.CreateSchema.class, ForyFactory.C_145);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.sequence.CreateSequence.class,
				ForyFactory.C_146);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.synonym.CreateSynonym.class,
				ForyFactory.C_147);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.CheckConstraint.class,
				ForyFactory.C_148);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ColDataType.class, ForyFactory.C_149);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ColumnDefinition.class,
				ForyFactory.C_150);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.CreateTable.class, ForyFactory.C_151);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ExcludeConstraint.class,
				ForyFactory.C_152);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ForeignKeyIndex.class,
				ForyFactory.C_153);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.Index.class, ForyFactory.C_154);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.Index.ColumnParams.class,
				ForyFactory.C_155);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.NamedConstraint.class,
				ForyFactory.C_156);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.RowMovement.class, ForyFactory.C_157);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.view.AlterView.class, ForyFactory.C_158);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.view.CreateView.class, ForyFactory.C_159);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.delete.Delete.class, ForyFactory.C_160);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.drop.Drop.class, ForyFactory.C_161);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.execute.Execute.class, ForyFactory.C_162);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.grant.Grant.class, ForyFactory.C_163);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.Insert.class, ForyFactory.C_164);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertConflictAction.class, ForyFactory.C_165);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertConflictTarget.class, ForyFactory.C_166);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.Merge.class, ForyFactory.C_167);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeDelete.class, ForyFactory.C_168);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeInsert.class, ForyFactory.C_169);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeUpdate.class, ForyFactory.C_170);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.refresh.RefreshMaterializedViewStatement.class,
				ForyFactory.C_171);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.AllColumns.class, ForyFactory.C_172);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.AllTableColumns.class, ForyFactory.C_173);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Distinct.class, ForyFactory.C_174);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ExceptOp.class, ForyFactory.C_175);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Fetch.class, ForyFactory.C_176);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.First.class, ForyFactory.C_177);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForClause.class, ForyFactory.C_178);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.GroupByElement.class, ForyFactory.C_179);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.IntersectOp.class, ForyFactory.C_180);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Join.class, ForyFactory.C_181);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLJoinWindow.class, ForyFactory.C_182);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLWindow.class, ForyFactory.C_183);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.LateralSubSelect.class, ForyFactory.C_184);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.LateralView.class, ForyFactory.C_185);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Limit.class, ForyFactory.C_186);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.MinusOp.class, ForyFactory.C_187);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Offset.class, ForyFactory.C_188);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OptimizeFor.class, ForyFactory.C_189);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OrderByElement.class, ForyFactory.C_190);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ParenthesedFromItem.class, ForyFactory.C_191);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ParenthesedSelect.class, ForyFactory.C_192);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Pivot.class, ForyFactory.C_193);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PivotXml.class, ForyFactory.C_194);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PlainSelect.class, ForyFactory.C_195);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SelectItem.class, ForyFactory.C_196);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperationList.class, ForyFactory.C_197);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperationList.SetOperationType.class,
				ForyFactory.C_198);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Skip.class, ForyFactory.C_199);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.TableFunction.class, ForyFactory.C_200);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.TableStatement.class, ForyFactory.C_201);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Top.class, ForyFactory.C_202);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.UnPivot.class, ForyFactory.C_203);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.UnionOp.class, ForyFactory.C_204);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Values.class, ForyFactory.C_205);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Wait.class, ForyFactory.C_206);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.WithIsolation.class, ForyFactory.C_207);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.WithItem.class, ForyFactory.C_208);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.show.ShowIndexStatement.class, ForyFactory.C_209);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.show.ShowTablesStatement.class, ForyFactory.C_210);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.truncate.Truncate.class, ForyFactory.C_211);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.Update.class, ForyFactory.C_212);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.UpdateSet.class, ForyFactory.C_213);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.upsert.Upsert.class, ForyFactory.C_214);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultiAndExpression.class, ForyFactory.C_215);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultiOrExpression.class, ForyFactory.C_216);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.BinaryExpression.class, ForyFactory.C_217);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ComparisonOperator.class,
				ForyFactory.C_218);
		ForyFactory.INSTANCE.register(
				net.sf.jsqlparser.expression.operators.relational.OldOracleJoinBinaryExpression.class,
				ForyFactory.C_219);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Function.NullHandling.class, ForyFactory.C_220);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.CreateFunctionalStatement.class, ForyFactory.C_221);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Select.class, ForyFactory.C_222);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperation.class, ForyFactory.C_223);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultipleExpression.class, ForyFactory.C_224);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertModifierPriority.class,
				ForyFactory.C_225);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OrderByElement.NullOrdering.class,
				ForyFactory.C_226);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForMode.class, ForyFactory.C_227);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.MySqlSqlCacheFlags.class, ForyFactory.C_228);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PlainSelect.BigQuerySelectQualifier.class,
				ForyFactory.C_229);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.UpdateModifierPriority.class,
				ForyFactory.C_230);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.LikeExpression.KeyWord.class,
				ForyFactory.C_231);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.delete.DeleteModifierPriority.class,
				ForyFactory.C_232);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Partition.class, ForyFactory.C_233);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.ConflictActionType.class, ForyFactory.C_234);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForClause.ForOption.class, ForyFactory.C_235);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLWindow.TimeUnit.class, ForyFactory.C_236);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.First.Keyword.class, ForyFactory.C_237);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowElement.Type.class, ForyFactory.C_238);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowOffset.Type.class, ForyFactory.C_239);
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
