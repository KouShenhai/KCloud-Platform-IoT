import React, { useEffect, useState } from 'react';
import { Checkbox, Col, Form, Modal, Row, Tree } from 'antd';
import { FormattedMessage, useIntl } from '@umijs/max';
import { Key, ProForm, ProFormDigit, ProFormSelect, ProFormText } from '@ant-design/pro-components';
import { DataNode } from 'antd/es/tree';
import { CheckboxValueType } from 'antd/es/checkbox/Group';

export type FormValueType = any & Partial<API.System.Dept>;

export type DataScopeFormProps = {
    onCancel: (flag?: boolean, formVals?: FormValueType) => void;
    onSubmit: (values: FormValueType) => Promise<void>;
    open: boolean;
    values: Partial<API.System.Role>;
    deptTree: DataNode[];
    deptCheckedKeys: string[];
};

const DataScopeForm: React.FC<DataScopeFormProps> = (props) => {
    const [form] = Form.useForm();

    const { deptTree, deptCheckedKeys } = props;
    const [dataScopeType, setDataScopeType] = useState<string | undefined>('1');
    const [deptIds, setDeptIds] = useState<string[] | {checked: string[], halfChecked: string[]}>([]);
    const [deptTreeExpandKey, setDeptTreeExpandKey] = useState<Key[]>([]);
    const [checkStrictly, setCheckStrictly] = useState<boolean>(true);


    useEffect(() => {
        setDeptIds(deptCheckedKeys);
        form.resetFields();
        form.setFieldsValue({
            roleId: props.values.roleId,
            roleName: props.values.roleName,
            roleKey: props.values.roleKey,
            dataScope: props.values.dataScope,
        });
        setDataScopeType(props.values.dataScope);
    }, [props.values]);

    const intl = useIntl();
    const handleOk = () => {
        form.submit();
    };
    const handleCancel = () => {
        props.onCancel();
    };
    const handleFinish = async (values: Record<string, any>) => {
        props.onSubmit({ ...values, deptIds } as FormValueType);
    };

    const getAllDeptNode = (node: DataNode[]) => {
        let keys: any[] = [];
        node.forEach(value => {
            keys.push(value.key);
            if(value.children) {
                keys = keys.concat(getAllDeptNode(value.children));
            }
        });
        return keys;
    }

    const deptAllNodes = getAllDeptNode(deptTree);


    const onDeptOptionChange = (checkedValues: CheckboxValueType[]) => {
        if(checkedValues.includes('deptExpand')) {
            setDeptTreeExpandKey(deptAllNodes);
        } else {
            setDeptTreeExpandKey([]);
        }
        if(checkedValues.includes('deptNodeAll')) {
            setDeptIds(deptAllNodes);
        } else {
            setDeptIds([]);
        }

        if(checkedValues.includes('deptCheckStrictly')) {
            setCheckStrictly(false);
        } else {
            setCheckStrictly(true);
        }
    };

    return (
        <Modal
            width={640}
            title={intl.formatMessage({
                id: 'system.user.auth.role',
                defaultMessage: '分配角色',
            })}
            open={props.open}
            destroyOnClose
            forceRender
            onOk={handleOk}
            onCancel={handleCancel}
        >
            <ProForm
                form={form}
                grid={true}
                layout="horizontal"
                onFinish={handleFinish}
                initialValues={{
                    login_password: '',
                    confirm_password: '',
                }}
            >

                <ProFormDigit
                    name="roleId"
                    label={intl.formatMessage({
                        id: 'system.role.role_id',
                        defaultMessage: '角色编号',
                    })}
                    colProps={{ md: 12, xl: 12 }}
                    placeholder="请输入角色编号"
                    disabled
                    hidden={true}
                    rules={[
                        {
                            required: false,
                            message: <FormattedMessage id="请输入角色编号！" defaultMessage="请输入角色编号！" />,
                        },
                    ]}
                />
                <ProFormText
                    name="roleName"
                    label={intl.formatMessage({
                        id: 'system.role.role_name',
                        defaultMessage: '角色名称',
                    })}
                    disabled
                    placeholder="请输入角色名称"
                    rules={[
                        {
                            required: true,
                            message: <FormattedMessage id="请输入角色名称！" defaultMessage="请输入角色名称！" />,
                        },
                    ]}
                />
                <ProFormText
                    name="roleKey"
                    label={intl.formatMessage({
                        id: 'system.role.role_key',
                        defaultMessage: '权限字符串',
                    })}
                    disabled
                    placeholder="请输入角色权限字符串"
                    rules={[
                        {
                            required: true,
                            message: <FormattedMessage id="请输入角色权限字符串！" defaultMessage="请输入角色权限字符串！" />,
                        },
                    ]}
                />
                <ProFormSelect
                    name="dataScope"
                    label='权限范围'
                    initialValue={'1'}
                    placeholder="请输入用户性别"
                    valueEnum={{
                        "1": "全部数据权限",
                        "2": "自定数据权限",
                        "3": "本部门数据权限",
                        "4": "本部门及以下数据权限",
                        "5": "仅本人数据权限"
                    }}
                    rules={[
                        {
                            required: true,
                        },
                    ]}
                    fieldProps={{
                        onChange: (value) => {
                            setDataScopeType(value);
                        },
                    }}
                />
                <ProForm.Item
                    name="deptIds"
                    label={intl.formatMessage({
                        id: 'system.role.auth',
                        defaultMessage: '菜单权限',
                    })}
                    required={dataScopeType === '1'}
                    hidden={dataScopeType !== '1'}
                >
                    <Row gutter={[16, 16]}>
                        <Col md={24}>
                            <Checkbox.Group
                                options={[
                                    { label: '展开/折叠', value: 'deptExpand' },
                                    { label: '全选/全不选', value: 'deptNodeAll' },
                                    // { label: '父子联动', value: 'deptCheckStrictly' },
                                ]}
                                onChange={onDeptOptionChange} />
                        </Col>
                        <Col md={24}>
                            <Tree
                                checkable={true}
                                checkStrictly={checkStrictly}
                                expandedKeys={deptTreeExpandKey}
                                treeData={deptTree}
                                checkedKeys={deptIds}
                                defaultCheckedKeys={deptCheckedKeys}
                                onCheck={(checkedKeys: any, checkInfo: any) => {
                                    console.log(checkedKeys, checkInfo);
                                    if(checkStrictly) {
                                        return setDeptIds(checkedKeys.checked);
                                    } else {
                                        return setDeptIds({checked: checkedKeys, halfChecked: checkInfo.halfCheckedKeys});
                                    }
                                }}
                                onExpand={(expandedKeys: Key[]) => {
                                    setDeptTreeExpandKey(deptTreeExpandKey.concat(expandedKeys));
                                }}
                            />
                        </Col>
                    </Row>
                </ProForm.Item>
            </ProForm>
        </Modal>
    );
};

export default DataScopeForm;
