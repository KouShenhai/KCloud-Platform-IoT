/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import org.apache.fury.Fury;
import org.apache.fury.ThreadSafeFury;
import org.apache.fury.config.CompatibleMode;
import org.apache.fury.config.Language;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public final class JSqlParserFuryFactory {

	private static final JSqlParserFuryFactory FACTORY = new JSqlParserFuryFactory();

	private final ThreadSafeFury fury = Fury.builder()
		.withLanguage(Language.JAVA)
		// enable reference tracking for shared/circular reference.
		// Disable it will have better performance if no duplicate reference.
		.withRefTracking(false)
		// compress int for smaller size
		// .withIntCompressed(true)
		// compress long for smaller size
		// .withLongCompressed(true)
		.withCompatibleMode(CompatibleMode.SCHEMA_CONSISTENT)
		// enable type forward/backward compatibility
		// disable it for small size and better performance.
		// .withCompatibleMode(CompatibleMode.COMPATIBLE)
		// enable async multi-threaded compilation.
		.withAsyncCompilation(true)
		.requireClassRegistration(true)
		.buildThreadSafeFury();

	private JSqlParserFuryFactory() {
		fury.register(net.sf.jsqlparser.expression.Alias.class);
		fury.register(net.sf.jsqlparser.expression.Alias.AliasColumn.class);
		fury.register(net.sf.jsqlparser.expression.AllValue.class);
		fury.register(net.sf.jsqlparser.expression.AnalyticExpression.class);
		fury.register(net.sf.jsqlparser.expression.AnyComparisonExpression.class);
		fury.register(net.sf.jsqlparser.expression.ArrayConstructor.class);
		fury.register(net.sf.jsqlparser.expression.ArrayExpression.class);
		fury.register(net.sf.jsqlparser.expression.CaseExpression.class);
		fury.register(net.sf.jsqlparser.expression.CastExpression.class);
		fury.register(net.sf.jsqlparser.expression.CollateExpression.class);
		fury.register(net.sf.jsqlparser.expression.ConnectByRootOperator.class);
		fury.register(net.sf.jsqlparser.expression.DateTimeLiteralExpression.class);
		fury.register(net.sf.jsqlparser.expression.DateValue.class);
		fury.register(net.sf.jsqlparser.expression.DoubleValue.class);
		fury.register(net.sf.jsqlparser.expression.ExtractExpression.class);
		fury.register(net.sf.jsqlparser.expression.FilterOverImpl.class);
		fury.register(net.sf.jsqlparser.expression.Function.class);
		fury.register(net.sf.jsqlparser.expression.HexValue.class);
		fury.register(net.sf.jsqlparser.expression.IntervalExpression.class);
		fury.register(net.sf.jsqlparser.expression.JdbcNamedParameter.class);
		fury.register(net.sf.jsqlparser.expression.JdbcParameter.class);
		fury.register(net.sf.jsqlparser.expression.JsonAggregateFunction.class);
		fury.register(net.sf.jsqlparser.expression.JsonExpression.class);
		fury.register(net.sf.jsqlparser.expression.JsonFunction.class);
		fury.register(net.sf.jsqlparser.expression.JsonFunctionExpression.class);
		fury.register(net.sf.jsqlparser.expression.JsonKeyValuePair.class);
		fury.register(net.sf.jsqlparser.expression.KeepExpression.class);
		fury.register(net.sf.jsqlparser.expression.LongValue.class);
		fury.register(net.sf.jsqlparser.expression.MySQLGroupConcat.class);
		fury.register(net.sf.jsqlparser.expression.MySQLIndexHint.class);
		fury.register(net.sf.jsqlparser.expression.NextValExpression.class);
		fury.register(net.sf.jsqlparser.expression.NotExpression.class);
		fury.register(net.sf.jsqlparser.expression.NullValue.class);
		fury.register(net.sf.jsqlparser.expression.NumericBind.class);
		fury.register(net.sf.jsqlparser.expression.OracleHierarchicalExpression.class);
		fury.register(net.sf.jsqlparser.expression.OracleHint.class);
		fury.register(net.sf.jsqlparser.expression.OracleNamedFunctionParameter.class);
		fury.register(net.sf.jsqlparser.expression.OrderByClause.class);
		fury.register(net.sf.jsqlparser.expression.OverlapsCondition.class);
		fury.register(net.sf.jsqlparser.expression.PartitionByClause.class);
		fury.register(net.sf.jsqlparser.expression.RangeExpression.class);
		fury.register(net.sf.jsqlparser.expression.RowConstructor.class);
		fury.register(net.sf.jsqlparser.expression.RowGetExpression.class);
		fury.register(net.sf.jsqlparser.expression.SQLServerHints.class);
		fury.register(net.sf.jsqlparser.expression.SignedExpression.class);
		fury.register(net.sf.jsqlparser.expression.StringValue.class);
		fury.register(net.sf.jsqlparser.expression.TimeKeyExpression.class);
		fury.register(net.sf.jsqlparser.expression.TimeValue.class);
		fury.register(net.sf.jsqlparser.expression.TimestampValue.class);
		fury.register(net.sf.jsqlparser.expression.TimezoneExpression.class);
		fury.register(net.sf.jsqlparser.expression.TranscodingFunction.class);
		fury.register(net.sf.jsqlparser.expression.TrimFunction.class);
		fury.register(net.sf.jsqlparser.expression.UserVariable.class);
		fury.register(net.sf.jsqlparser.expression.VariableAssignment.class);
		fury.register(net.sf.jsqlparser.expression.WhenClause.class);
		fury.register(net.sf.jsqlparser.expression.WindowDefinition.class);
		fury.register(net.sf.jsqlparser.expression.WindowElement.class);
		fury.register(net.sf.jsqlparser.expression.WindowOffset.class);
		fury.register(net.sf.jsqlparser.expression.WindowRange.class);
		fury.register(net.sf.jsqlparser.expression.XMLSerializeExpr.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.Addition.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.Concat.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.Division.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.IntegerDivision.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.Modulo.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.Multiplication.class);
		fury.register(net.sf.jsqlparser.expression.operators.arithmetic.Subtraction.class);
		fury.register(net.sf.jsqlparser.expression.operators.conditional.AndExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.conditional.OrExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.conditional.XorExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.Between.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.ContainedBy.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.Contains.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.DoubleAnd.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.EqualsTo.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.ExistsExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.ExpressionList.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.FullTextSearch.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.GeometryDistance.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.GreaterThan.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.InExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.IsBooleanExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.IsDistinctExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.IsNullExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.JsonOperator.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.LikeExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.Matches.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.MemberOfExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.MinorThan.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.MinorThanEquals.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.NamedExpressionList.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.NotEqualsTo.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.SimilarToExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.TSQLLeftJoin.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.TSQLRightJoin.class);
		fury.register(net.sf.jsqlparser.parser.ASTNodeAccessImpl.class);
		fury.register(net.sf.jsqlparser.parser.Token.class);
		fury.register(net.sf.jsqlparser.schema.Column.class);
		fury.register(net.sf.jsqlparser.schema.Sequence.class);
		fury.register(net.sf.jsqlparser.schema.Synonym.class);
		fury.register(net.sf.jsqlparser.schema.Table.class);
		fury.register(net.sf.jsqlparser.statement.Block.class);
		fury.register(net.sf.jsqlparser.statement.Commit.class);
		fury.register(net.sf.jsqlparser.statement.DeclareStatement.class);
		fury.register(net.sf.jsqlparser.statement.DeclareStatement.TypeDefExpr.class);
		fury.register(net.sf.jsqlparser.statement.DescribeStatement.class);
		fury.register(net.sf.jsqlparser.statement.ExplainStatement.class);
		fury.register(net.sf.jsqlparser.statement.ExplainStatement.Option.class);
		fury.register(net.sf.jsqlparser.statement.IfElseStatement.class);
		fury.register(net.sf.jsqlparser.statement.OutputClause.class);
		fury.register(net.sf.jsqlparser.statement.PurgeStatement.class);
		fury.register(net.sf.jsqlparser.statement.ReferentialAction.class);
		fury.register(net.sf.jsqlparser.statement.ResetStatement.class);
		fury.register(net.sf.jsqlparser.statement.RollbackStatement.class);
		fury.register(net.sf.jsqlparser.statement.SavepointStatement.class);
		fury.register(net.sf.jsqlparser.statement.SetStatement.class);
		fury.register(net.sf.jsqlparser.statement.ShowColumnsStatement.class);
		fury.register(net.sf.jsqlparser.statement.ShowStatement.class);
		fury.register(net.sf.jsqlparser.statement.Statements.class);
		fury.register(net.sf.jsqlparser.statement.UnsupportedStatement.class);
		fury.register(net.sf.jsqlparser.statement.UseStatement.class);
		fury.register(net.sf.jsqlparser.statement.alter.Alter.class);
		fury.register(net.sf.jsqlparser.statement.alter.AlterExpression.class);
		fury.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDataType.class);
		fury.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDropDefault.class);
		fury.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDropNotNull.class);
		fury.register(net.sf.jsqlparser.statement.alter.AlterSession.class);
		fury.register(net.sf.jsqlparser.statement.alter.AlterSystemStatement.class);
		fury.register(net.sf.jsqlparser.statement.alter.RenameTableStatement.class);
		fury.register(net.sf.jsqlparser.statement.alter.sequence.AlterSequence.class);
		fury.register(net.sf.jsqlparser.statement.analyze.Analyze.class);
		fury.register(net.sf.jsqlparser.statement.comment.Comment.class);
		fury.register(net.sf.jsqlparser.statement.create.function.CreateFunction.class);
		fury.register(net.sf.jsqlparser.statement.create.index.CreateIndex.class);
		fury.register(net.sf.jsqlparser.statement.create.procedure.CreateProcedure.class);
		fury.register(net.sf.jsqlparser.statement.create.schema.CreateSchema.class);
		fury.register(net.sf.jsqlparser.statement.create.sequence.CreateSequence.class);
		fury.register(net.sf.jsqlparser.statement.create.synonym.CreateSynonym.class);
		fury.register(net.sf.jsqlparser.statement.create.table.CheckConstraint.class);
		fury.register(net.sf.jsqlparser.statement.create.table.ColDataType.class);
		fury.register(net.sf.jsqlparser.statement.create.table.ColumnDefinition.class);
		fury.register(net.sf.jsqlparser.statement.create.table.CreateTable.class);
		fury.register(net.sf.jsqlparser.statement.create.table.ExcludeConstraint.class);
		fury.register(net.sf.jsqlparser.statement.create.table.ForeignKeyIndex.class);
		fury.register(net.sf.jsqlparser.statement.create.table.Index.class);
		fury.register(net.sf.jsqlparser.statement.create.table.Index.ColumnParams.class);
		fury.register(net.sf.jsqlparser.statement.create.table.NamedConstraint.class);
		fury.register(net.sf.jsqlparser.statement.create.table.RowMovement.class);
		fury.register(net.sf.jsqlparser.statement.create.view.AlterView.class);
		fury.register(net.sf.jsqlparser.statement.create.view.CreateView.class);
		fury.register(net.sf.jsqlparser.statement.delete.Delete.class);
		fury.register(net.sf.jsqlparser.statement.drop.Drop.class);
		fury.register(net.sf.jsqlparser.statement.execute.Execute.class);
		fury.register(net.sf.jsqlparser.statement.grant.Grant.class);
		fury.register(net.sf.jsqlparser.statement.insert.Insert.class);
		fury.register(net.sf.jsqlparser.statement.insert.InsertConflictAction.class);
		fury.register(net.sf.jsqlparser.statement.insert.InsertConflictTarget.class);
		fury.register(net.sf.jsqlparser.statement.merge.Merge.class);
		fury.register(net.sf.jsqlparser.statement.merge.MergeDelete.class);
		fury.register(net.sf.jsqlparser.statement.merge.MergeInsert.class);
		fury.register(net.sf.jsqlparser.statement.merge.MergeUpdate.class);
		fury.register(net.sf.jsqlparser.statement.refresh.RefreshMaterializedViewStatement.class);
		fury.register(net.sf.jsqlparser.statement.select.AllColumns.class);
		fury.register(net.sf.jsqlparser.statement.select.AllTableColumns.class);
		fury.register(net.sf.jsqlparser.statement.select.Distinct.class);
		fury.register(net.sf.jsqlparser.statement.select.ExceptOp.class);
		fury.register(net.sf.jsqlparser.statement.select.Fetch.class);
		fury.register(net.sf.jsqlparser.statement.select.First.class);
		fury.register(net.sf.jsqlparser.statement.select.ForClause.class);
		fury.register(net.sf.jsqlparser.statement.select.GroupByElement.class);
		fury.register(net.sf.jsqlparser.statement.select.IntersectOp.class);
		fury.register(net.sf.jsqlparser.statement.select.Join.class);
		fury.register(net.sf.jsqlparser.statement.select.KSQLJoinWindow.class);
		fury.register(net.sf.jsqlparser.statement.select.KSQLWindow.class);
		fury.register(net.sf.jsqlparser.statement.select.LateralSubSelect.class);
		fury.register(net.sf.jsqlparser.statement.select.LateralView.class);
		fury.register(net.sf.jsqlparser.statement.select.Limit.class);
		fury.register(net.sf.jsqlparser.statement.select.MinusOp.class);
		fury.register(net.sf.jsqlparser.statement.select.Offset.class);
		fury.register(net.sf.jsqlparser.statement.select.OptimizeFor.class);
		fury.register(net.sf.jsqlparser.statement.select.OrderByElement.class);
		fury.register(net.sf.jsqlparser.statement.select.ParenthesedFromItem.class);
		fury.register(net.sf.jsqlparser.statement.select.ParenthesedSelect.class);
		fury.register(net.sf.jsqlparser.statement.select.Pivot.class);
		fury.register(net.sf.jsqlparser.statement.select.PivotXml.class);
		fury.register(net.sf.jsqlparser.statement.select.PlainSelect.class);
		fury.register(net.sf.jsqlparser.statement.select.SelectItem.class);
		fury.register(net.sf.jsqlparser.statement.select.SetOperationList.class);
		fury.register(net.sf.jsqlparser.statement.select.SetOperationList.SetOperationType.class);
		fury.register(net.sf.jsqlparser.statement.select.Skip.class);
		fury.register(net.sf.jsqlparser.statement.select.TableFunction.class);
		fury.register(net.sf.jsqlparser.statement.select.TableStatement.class);
		fury.register(net.sf.jsqlparser.statement.select.Top.class);
		fury.register(net.sf.jsqlparser.statement.select.UnPivot.class);
		fury.register(net.sf.jsqlparser.statement.select.UnionOp.class);
		fury.register(net.sf.jsqlparser.statement.select.Values.class);
		fury.register(net.sf.jsqlparser.statement.select.Wait.class);
		fury.register(net.sf.jsqlparser.statement.select.WithIsolation.class);
		fury.register(net.sf.jsqlparser.statement.select.WithItem.class);
		fury.register(net.sf.jsqlparser.statement.show.ShowIndexStatement.class);
		fury.register(net.sf.jsqlparser.statement.show.ShowTablesStatement.class);
		fury.register(net.sf.jsqlparser.statement.truncate.Truncate.class);
		fury.register(net.sf.jsqlparser.statement.update.Update.class);
		fury.register(net.sf.jsqlparser.statement.update.UpdateSet.class);
		fury.register(net.sf.jsqlparser.statement.upsert.Upsert.class);
		fury.register(net.sf.jsqlparser.util.cnfexpression.MultiAndExpression.class);
		fury.register(net.sf.jsqlparser.util.cnfexpression.MultiOrExpression.class);
		fury.register(net.sf.jsqlparser.expression.BinaryExpression.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.ComparisonOperator.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.OldOracleJoinBinaryExpression.class);
		fury.register(net.sf.jsqlparser.expression.Function.NullHandling.class);
		fury.register(net.sf.jsqlparser.statement.CreateFunctionalStatement.class);
		fury.register(net.sf.jsqlparser.statement.select.Select.class);
		fury.register(net.sf.jsqlparser.statement.select.SetOperation.class);
		fury.register(net.sf.jsqlparser.util.cnfexpression.MultipleExpression.class);
		fury.register(net.sf.jsqlparser.statement.insert.InsertModifierPriority.class);
		fury.register(net.sf.jsqlparser.statement.select.OrderByElement.NullOrdering.class);
		fury.register(net.sf.jsqlparser.statement.select.ForMode.class);
		fury.register(net.sf.jsqlparser.statement.select.MySqlSqlCacheFlags.class);
		fury.register(net.sf.jsqlparser.statement.select.PlainSelect.BigQuerySelectQualifier.class);
		fury.register(net.sf.jsqlparser.statement.update.UpdateModifierPriority.class);
		fury.register(net.sf.jsqlparser.expression.operators.relational.LikeExpression.KeyWord.class);
		fury.register(net.sf.jsqlparser.statement.delete.DeleteModifierPriority.class);
		fury.register(net.sf.jsqlparser.schema.Partition.class);
		fury.register(net.sf.jsqlparser.statement.insert.ConflictActionType.class);
		fury.register(net.sf.jsqlparser.statement.select.ForClause.ForOption.class);
		fury.register(net.sf.jsqlparser.statement.select.KSQLWindow.TimeUnit.class);
		fury.register(net.sf.jsqlparser.statement.select.First.Keyword.class);
		fury.register(net.sf.jsqlparser.expression.WindowElement.Type.class);
		fury.register(net.sf.jsqlparser.expression.WindowOffset.Type.class);
	}

	public static JSqlParserFuryFactory getFuryFactory() {
		return FACTORY;
	}

	public byte[] serialize(Object object) {
		if (object == null) {
			return new byte[0];
		}
		if (object instanceof String str) {
			return str.getBytes(StandardCharsets.UTF_8);
		}
		return fury.serialize(object);
	}

	public Object deserialize(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		return fury.deserialize(bytes);
	}

}
