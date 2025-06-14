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

import com.baomidou.mybatisplus.extension.parser.cache.AbstractCaffeineJsqlParseCache;
import com.github.benmanes.caffeine.cache.Cache;
import org.laokou.common.fury.config.FuryFactory;

/**
 * jsqlparser 缓存 fury 序列化 Caffeine 缓存实现.
 *
 * @author laokou
 */
public class FurySerialCaffeineJsqlParseCache extends AbstractCaffeineJsqlParseCache {

	static {
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.Alias.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.Alias.AliasColumn.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.AllValue.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.AnalyticExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.AnyComparisonExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.ArrayConstructor.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.ArrayExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.CaseExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.CastExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.CollateExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.ConnectByRootOperator.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.DateTimeLiteralExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.DateValue.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.DoubleValue.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.ExtractExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.FilterOverImpl.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.Function.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.HexValue.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.IntervalExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.JdbcNamedParameter.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.JdbcParameter.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonAggregateFunction.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonFunction.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonFunctionExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonKeyValuePair.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.KeepExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.LongValue.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.MySQLGroupConcat.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.MySQLIndexHint.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.NextValExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.NotExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.NullValue.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.NumericBind.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleHierarchicalExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleHint.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleNamedFunctionParameter.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.OrderByClause.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.OverlapsCondition.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.PartitionByClause.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.RangeExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.RowConstructor.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.RowGetExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.SQLServerHints.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.SignedExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.StringValue.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimeKeyExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimeValue.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimestampValue.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimezoneExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.TranscodingFunction.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.TrimFunction.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.UserVariable.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.VariableAssignment.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.WhenClause.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowDefinition.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowElement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowOffset.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowRange.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.XMLSerializeExpr.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Addition.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Concat.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Division.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.IntegerDivision.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Modulo.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Multiplication.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Subtraction.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.AndExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.OrExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.XorExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Between.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ContainedBy.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Contains.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.DoubleAnd.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.EqualsTo.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ExistsExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ExpressionList.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.FullTextSearch.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GeometryDistance.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GreaterThan.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.InExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsBooleanExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsDistinctExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsNullExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.JsonOperator.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.LikeExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Matches.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MemberOfExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MinorThan.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MinorThanEquals.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.NamedExpressionList.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.NotEqualsTo.class);
		FuryFactory.INSTANCE
			.register(net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.SimilarToExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.TSQLLeftJoin.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.TSQLRightJoin.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.parser.ASTNodeAccessImpl.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.parser.Token.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.schema.Column.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.schema.Sequence.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.schema.Synonym.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.schema.Table.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.Block.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.Commit.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.DeclareStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.DeclareStatement.TypeDefExpr.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.DescribeStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.ExplainStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.ExplainStatement.Option.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.IfElseStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.OutputClause.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.PurgeStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.ReferentialAction.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.ResetStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.RollbackStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.SavepointStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.SetStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.ShowColumnsStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.ShowStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.Statements.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.UnsupportedStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.UseStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.Alter.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDataType.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDropDefault.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDropNotNull.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterSession.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterSystemStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.RenameTableStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.sequence.AlterSequence.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.analyze.Analyze.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.comment.Comment.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.function.CreateFunction.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.index.CreateIndex.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.procedure.CreateProcedure.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.schema.CreateSchema.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.sequence.CreateSequence.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.synonym.CreateSynonym.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.CheckConstraint.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ColDataType.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ColumnDefinition.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.CreateTable.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ExcludeConstraint.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ForeignKeyIndex.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.Index.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.Index.ColumnParams.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.NamedConstraint.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.RowMovement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.view.AlterView.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.view.CreateView.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.delete.Delete.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.drop.Drop.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.execute.Execute.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.grant.Grant.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.Insert.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertConflictAction.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertConflictTarget.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.Merge.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeDelete.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeInsert.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeUpdate.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.refresh.RefreshMaterializedViewStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.AllColumns.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.AllTableColumns.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Distinct.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ExceptOp.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Fetch.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.First.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForClause.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.GroupByElement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.IntersectOp.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Join.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLJoinWindow.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLWindow.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.LateralSubSelect.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.LateralView.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Limit.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.MinusOp.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Offset.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OptimizeFor.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OrderByElement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ParenthesedFromItem.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ParenthesedSelect.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Pivot.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PivotXml.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PlainSelect.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SelectItem.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperationList.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperationList.SetOperationType.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Skip.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.TableFunction.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.TableStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Top.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.UnPivot.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.UnionOp.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Values.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Wait.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.WithIsolation.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.WithItem.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.show.ShowIndexStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.show.ShowTablesStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.truncate.Truncate.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.Update.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.UpdateSet.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.upsert.Upsert.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultiAndExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultiOrExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.BinaryExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ComparisonOperator.class);
		FuryFactory.INSTANCE
			.register(net.sf.jsqlparser.expression.operators.relational.OldOracleJoinBinaryExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.Function.NullHandling.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.CreateFunctionalStatement.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Select.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperation.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultipleExpression.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertModifierPriority.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OrderByElement.NullOrdering.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForMode.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.MySqlSqlCacheFlags.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PlainSelect.BigQuerySelectQualifier.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.UpdateModifierPriority.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.LikeExpression.KeyWord.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.delete.DeleteModifierPriority.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.schema.Partition.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.ConflictActionType.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForClause.ForOption.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLWindow.TimeUnit.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.First.Keyword.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowElement.Type.class);
		FuryFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowOffset.Type.class);
	}

	public FurySerialCaffeineJsqlParseCache(Cache<String, byte[]> cache) {
		super(cache);
	}

	@Override
	public byte[] serialize(Object obj) {
		return FuryFactory.INSTANCE.serialize(obj);
	}

	@Override
	public Object deserialize(String sql, byte[] bytes) {
		return FuryFactory.INSTANCE.deserialize(bytes);
	}

}
