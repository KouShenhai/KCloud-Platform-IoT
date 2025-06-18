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
import org.laokou.common.fory.config.ForyFactory;

/**
 * jsqlparser 缓存 fory 序列化 Caffeine 缓存实现.
 *
 * @author laokou
 */
public class ForySerialCaffeineJsqlParseCache extends AbstractCaffeineJsqlParseCache {

	static {
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Alias.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Alias.AliasColumn.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.AllValue.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.AnalyticExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.AnyComparisonExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ArrayConstructor.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ArrayExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.CaseExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.CastExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.CollateExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ConnectByRootOperator.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.DateTimeLiteralExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.DateValue.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.DoubleValue.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.ExtractExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.FilterOverImpl.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Function.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.HexValue.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.IntervalExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JdbcNamedParameter.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JdbcParameter.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonAggregateFunction.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonFunction.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonFunctionExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.JsonKeyValuePair.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.KeepExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.LongValue.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.MySQLGroupConcat.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.MySQLIndexHint.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NextValExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NotExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NullValue.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.NumericBind.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleHierarchicalExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleHint.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OracleNamedFunctionParameter.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OrderByClause.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.OverlapsCondition.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.PartitionByClause.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.RangeExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.RowConstructor.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.RowGetExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.SQLServerHints.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.SignedExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.StringValue.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimeKeyExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimeValue.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimestampValue.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TimezoneExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TranscodingFunction.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.TrimFunction.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.UserVariable.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.VariableAssignment.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WhenClause.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowDefinition.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowElement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowOffset.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowRange.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.XMLSerializeExpr.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Addition.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Concat.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Division.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.IntegerDivision.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Modulo.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Multiplication.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.arithmetic.Subtraction.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.AndExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.OrExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.conditional.XorExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Between.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ContainedBy.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Contains.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.DoubleAnd.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.EqualsTo.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ExistsExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ExpressionList.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.FullTextSearch.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GeometryDistance.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GreaterThan.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.InExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsBooleanExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsDistinctExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.IsNullExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.JsonOperator.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.LikeExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.Matches.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MemberOfExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MinorThan.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.MinorThanEquals.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.NamedExpressionList.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.NotEqualsTo.class);
		ForyFactory.INSTANCE
			.register(net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.SimilarToExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.TSQLLeftJoin.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.TSQLRightJoin.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.parser.ASTNodeAccessImpl.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.parser.Token.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Column.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Sequence.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Synonym.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Table.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.Block.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.Commit.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.DeclareStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.DeclareStatement.TypeDefExpr.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.DescribeStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ExplainStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ExplainStatement.Option.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.IfElseStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.OutputClause.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.PurgeStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ReferentialAction.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ResetStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.RollbackStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.SavepointStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.SetStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ShowColumnsStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.ShowStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.Statements.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.UnsupportedStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.UseStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.Alter.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDataType.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDropDefault.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDropNotNull.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterSession.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.AlterSystemStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.RenameTableStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.alter.sequence.AlterSequence.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.analyze.Analyze.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.comment.Comment.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.function.CreateFunction.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.index.CreateIndex.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.procedure.CreateProcedure.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.schema.CreateSchema.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.sequence.CreateSequence.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.synonym.CreateSynonym.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.CheckConstraint.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ColDataType.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ColumnDefinition.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.CreateTable.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ExcludeConstraint.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.ForeignKeyIndex.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.Index.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.Index.ColumnParams.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.NamedConstraint.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.table.RowMovement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.view.AlterView.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.create.view.CreateView.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.delete.Delete.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.drop.Drop.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.execute.Execute.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.grant.Grant.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.Insert.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertConflictAction.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertConflictTarget.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.Merge.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeDelete.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeInsert.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.merge.MergeUpdate.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.refresh.RefreshMaterializedViewStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.AllColumns.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.AllTableColumns.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Distinct.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ExceptOp.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Fetch.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.First.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForClause.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.GroupByElement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.IntersectOp.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Join.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLJoinWindow.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLWindow.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.LateralSubSelect.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.LateralView.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Limit.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.MinusOp.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Offset.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OptimizeFor.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OrderByElement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ParenthesedFromItem.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ParenthesedSelect.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Pivot.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PivotXml.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PlainSelect.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SelectItem.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperationList.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperationList.SetOperationType.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Skip.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.TableFunction.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.TableStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Top.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.UnPivot.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.UnionOp.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Values.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Wait.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.WithIsolation.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.WithItem.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.show.ShowIndexStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.show.ShowTablesStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.truncate.Truncate.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.Update.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.UpdateSet.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.upsert.Upsert.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultiAndExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultiOrExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.BinaryExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.ComparisonOperator.class);
		ForyFactory.INSTANCE
			.register(net.sf.jsqlparser.expression.operators.relational.OldOracleJoinBinaryExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.Function.NullHandling.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.CreateFunctionalStatement.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.Select.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.SetOperation.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.util.cnfexpression.MultipleExpression.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.InsertModifierPriority.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.OrderByElement.NullOrdering.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForMode.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.MySqlSqlCacheFlags.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.PlainSelect.BigQuerySelectQualifier.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.update.UpdateModifierPriority.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.operators.relational.LikeExpression.KeyWord.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.delete.DeleteModifierPriority.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.schema.Partition.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.insert.ConflictActionType.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.ForClause.ForOption.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.KSQLWindow.TimeUnit.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.statement.select.First.Keyword.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowElement.Type.class);
		ForyFactory.INSTANCE.register(net.sf.jsqlparser.expression.WindowOffset.Type.class);
	}

	public ForySerialCaffeineJsqlParseCache(Cache<String, byte[]> cache) {
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
