/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.bricks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import org.catrobat.catroid.formulaeditor.Operators;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.ui.dialogs.NewDataDialog;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;
import org.catrobat.catroid.utils.IconsUtil;
import org.catrobat.catroid.utils.SimpleBrickUtil;
import org.catrobat.catroid.utils.TextSizeUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.catrobat.catroid.utils.SimpleBrickUtil.createArrayAdapterOperator;
import static org.catrobat.catroid.utils.SimpleBrickUtil.isInFormulaEditor;

public class RepeatUntilSimpleBrick extends FormulaBrick implements LoopBeginBrick {
	private static final long serialVersionUID = 1L;

	protected transient LoopEndBrick loopEndBrick;
	private transient long beginLoopTime;

	private transient LoopBeginBrick copy;

	private transient String textViewSelectionFirstGroup = "";
	private transient String textViewSelectionFirst = "";
	private transient String textViewSelectionSecondGroup = "";
	private transient String textViewSelectionSecond = "";
	private transient int spinnerSelectionOperator;
	private transient String spinnerSelectionOperatorString = Operators.GREATER_THAN.toString();

	public RepeatUntilSimpleBrick(Formula simpleCondition, Formula firstCondition,
			Formula secondCondition, Formula operatorCondition) {
		initializeBrickFields(simpleCondition, firstCondition, secondCondition, operatorCondition);
	}

	public void initializeBrickFields(Formula simpleCondition, Formula firstCondition,
			Formula secondCondition, Formula operatorCondition) {
		addAllowedBrickField(BrickField.REPEAT_UNTIL_CONDITION);
		addAllowedBrickField(BrickField.IF_SIMPLE_FIRST_CONDITION);
		addAllowedBrickField(BrickField.IF_SIMPLE_SECOND_CONDITION);
		addAllowedBrickField(BrickField.IF_SIMPLE_OPERATOR_CONDITION);

		setFormulaWithBrickField(BrickField.REPEAT_UNTIL_CONDITION, simpleCondition);
		setFormulaWithBrickField(BrickField.IF_SIMPLE_FIRST_CONDITION, firstCondition);
		setFormulaWithBrickField(BrickField.IF_SIMPLE_SECOND_CONDITION, secondCondition);
		setFormulaWithBrickField(BrickField.IF_SIMPLE_OPERATOR_CONDITION, operatorCondition);
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.REPEAT_UNTIL_CONDITION).getRequiredResources()
				| getFormulaWithBrickField(BrickField.IF_SIMPLE_FIRST_CONDITION).getRequiredResources()
				| getFormulaWithBrickField(BrickField.IF_SIMPLE_SECOND_CONDITION).getRequiredResources()
				| getFormulaWithBrickField(BrickField.IF_SIMPLE_OPERATOR_CONDITION).getRequiredResources();
	}

	@Override
	public View getView(final Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, R.layout.brick_repeat_until_simple, null);
		view = BrickViewProvider.setAlphaOnView(view, alphaValue);

		IconsUtil.addIcon(context, (TextView) view.findViewById(R.id.brick_repeat_until_label_simple),
				context.getString(R.string.category_control));

		setCheckboxView(R.id.brick_repeat_until_checkbox_simple);

		TextView firstCondition = (TextView) view.findViewById(R.id.brick_repeat_begin_first_part_text_view);
		getFormulaWithBrickField(BrickField.IF_SIMPLE_FIRST_CONDITION).setTextFieldId(R.id.brick_repeat_begin_first_part_text_view);
		getFormulaWithBrickField(BrickField.IF_SIMPLE_FIRST_CONDITION).refreshTextField(view);

		TextView secondCondition = (TextView) view.findViewById(R.id.brick_repeat_begin_second_part_text_view);
		getFormulaWithBrickField(BrickField.IF_SIMPLE_SECOND_CONDITION).setTextFieldId(R.id.brick_repeat_begin_second_part_text_view);
		getFormulaWithBrickField(BrickField.IF_SIMPLE_SECOND_CONDITION).refreshTextField(view);

		ArrayAdapter<String> spinnerAdapterOperator = createArrayAdapterOperator(context);
		SimpleBrickUtil.SpinnerAdapterWrapper spinnerAdapterWrapperOperator = new SimpleBrickUtil.SpinnerAdapterWrapper(context,
				spinnerAdapterOperator);

		Spinner ifBeginOperatorSpinner = (Spinner) view.findViewById(R.id.brick_repeat_begin_operator_spinner);

		final ExpandableListView myListFirst = new ExpandableListView(context);
		final ExpandableListView myListSecond = new ExpandableListView(context);
		final AlertDialog.Builder builderFirst = new AlertDialog.Builder(context);
		final AlertDialog.Builder builderSecond = new AlertDialog.Builder(context);
		final SimpleBrickUtil.MyExpandableListAdapter mAdapterFirst = new SimpleBrickUtil.MyExpandableListAdapter(context);
		final SimpleBrickUtil.MyExpandableListAdapter mAdapterSecond = new SimpleBrickUtil.MyExpandableListAdapter(context);

		myListFirst.setAdapter(mAdapterFirst);
		builderFirst.setView(myListFirst);
		final AlertDialog dialogFirst = builderFirst.create();

		myListSecond.setAdapter(mAdapterSecond);
		builderSecond.setView(myListSecond);
		final AlertDialog dialogSecond = builderSecond.create();

		ifBeginOperatorSpinner.setAdapter(spinnerAdapterWrapperOperator);
		ifBeginOperatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String itemSelected = parent.getSelectedItem().toString();
				spinnerSelectionOperator = position;
				spinnerSelectionOperatorString = itemSelected;
				setNewOperator();
				updateFormula();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
			}
		});
		ifBeginOperatorSpinner.setSelection(spinnerSelectionOperator);

		firstCondition.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				myListFirst.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
						textViewSelectionFirst = parent.getExpandableListAdapter().getChild(groupPosition,
								childPosition).toString();

						if (!textViewSelectionFirst.equals(context.getString(R.string.simple_brick_new_data_dialog))) {
							dialogFirst.cancel();
							setNewFormula(context, textViewSelectionFirstGroup, textViewSelectionFirst, true);
							updateFormula();
						} else if (textViewSelectionFirst.equals(context.getString(R.string.simple_brick_new_data_dialog))) {
							NewDataDialog dataDialog = new NewDataDialog(NewDataDialog.DialogType.SHOW_LIST_CHECKBOX);
							dataDialog.addUserListDialogListener(new NewDataDialog.NewUserListDialogListener() {
								@Override
								public void onFinishNewUserListDialog(Spinner spinnerToUpdate, UserList newUserList) {
									textViewSelectionFirst = newUserList.getName();
									setNewFormula(context, textViewSelectionFirstGroup, textViewSelectionFirst, true);
								}
							});
							dataDialog.addVariableDialogListener(new NewDataDialog.NewVariableDialogListener() {
								@Override
								public void onFinishNewVariableDialog(Spinner spinnerToUpdate, UserVariable newUserVariable) {
									textViewSelectionFirst = newUserVariable.getName();
									setNewFormula(context, textViewSelectionFirstGroup, textViewSelectionFirst, true);
								}
							});
							dataDialog.show(((Activity) view.getContext()).getFragmentManager(), NewDataDialog.DIALOG_FRAGMENT_TAG);
							dialogFirst.cancel();
						}
						return true;
					}
				});
				myListFirst.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent, View viewGroup, int groupPosition, long id) {
						textViewSelectionFirstGroup = parent.getExpandableListAdapter().getGroup(groupPosition).toString();

						if (textViewSelectionFirstGroup.equals(context.getString(R.string.simple_brick_group_number))) {
							dialogFirst.cancel();
							showFormulaEditorToEditFormula(view);
							return true;
						}
						return false;
					}
				});
				if (!isInFormulaEditor) {
					dialogFirst.show();
				} else {
					showFormulaEditorToEditFormula(view);
				}
			}
		});

		secondCondition.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				myListSecond.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
						textViewSelectionSecond = parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString();

						if (!textViewSelectionSecond.equals(context.getString(R.string
								.simple_brick_new_data_dialog))) {
							dialogSecond.cancel();
							setNewFormula(context, textViewSelectionSecondGroup, textViewSelectionSecond, false);
							updateFormula();
						} else if (textViewSelectionSecond.equals(context.getString(R.string.simple_brick_new_data_dialog))) {
							NewDataDialog dataDialog = new NewDataDialog(NewDataDialog.DialogType.SHOW_LIST_CHECKBOX);
							dataDialog.addUserListDialogListener(new NewDataDialog.NewUserListDialogListener() {
								@Override
								public void onFinishNewUserListDialog(Spinner spinnerToUpdate, UserList newUserList) {
									textViewSelectionSecond = newUserList.getName();
									setNewFormula(context, textViewSelectionSecondGroup, textViewSelectionSecond, false);
								}
							});
							dataDialog.addVariableDialogListener(new NewDataDialog.NewVariableDialogListener() {
								@Override
								public void onFinishNewVariableDialog(Spinner spinnerToUpdate, UserVariable newUserVariable) {
									textViewSelectionSecond = newUserVariable.getName();
									setNewFormula(context, textViewSelectionSecondGroup, textViewSelectionSecond, false);
								}
							});
							dataDialog.show(((Activity) view.getContext()).getFragmentManager(), NewDataDialog.DIALOG_FRAGMENT_TAG);
							dialogSecond.cancel();
						}
						return true;
					}
				});
				myListSecond.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent, View viewGroup, int groupPosition, long id) {
						textViewSelectionSecondGroup = parent.getExpandableListAdapter().getGroup(groupPosition).toString();
						if (textViewSelectionSecondGroup.equals(context.getString(R.string.simple_brick_group_number))) {
							dialogSecond.cancel();
							showFormulaEditorToEditFormula(view);
							return true;
						}
						return false;
					}
				});
				if (!isInFormulaEditor) {
					dialogSecond.show();
				} else {
					showFormulaEditorToEditFormula(view);
				}
			}
		});
		updateFormula();

		TextSizeUtil.enlargeViewGroup((ViewGroup) view);

		return view;
	}

	private void setNewOperator() {
		String operator;
		switch (spinnerSelectionOperatorString) {
			case "=":
				operator = Operators.EQUAL.name();
				break;
			case "≠":
				operator = Operators.NOT_EQUAL.name();
				break;
			case "<":
				operator = Operators.SMALLER_THAN.name();
				break;
			case "≤":
				operator = Operators.SMALLER_OR_EQUAL.name();
				break;
			case ">":
				operator = Operators.GREATER_THAN.name();
				break;
			case "≥":
				operator = Operators.GREATER_OR_EQUAL.name();
				break;
			default:
				operator = spinnerSelectionOperatorString;
				break;
		}
		FormulaElement operatorElement = new FormulaElement(FormulaElement.ElementType.OPERATOR, operator, null);
		setFormulaWithBrickField(BrickField.IF_SIMPLE_OPERATOR_CONDITION, new Formula(operatorElement));
	}

	private void setNewFormula(Context context, String textViewSelectionGroup, String textViewSelection, boolean
			isFirst) {
		FormulaElement ifCondition = null;

		if (textViewSelectionGroup.equals(context.getString(R.string.simple_brick_group_object))) {
			ifCondition = new FormulaElement(FormulaElement.ElementType.SENSOR, SimpleBrickUtil
					.getSensorByString(context, textViewSelection), null);
		} else if (textViewSelectionGroup.equals(context.getString(R.string.simple_brick_group_device))) {
			ifCondition = new FormulaElement(FormulaElement.ElementType.SENSOR, SimpleBrickUtil
					.getSensorByString(context, textViewSelection), null);
		} else if (textViewSelectionGroup.equals(context.getString(R.string.simple_brick_group_data))) {
			if (SimpleBrickUtil.isUserVariable(textViewSelection)) {
				ifCondition = new FormulaElement(FormulaElement.ElementType.USER_VARIABLE, textViewSelection, null);
			} else {
				ifCondition = new FormulaElement(FormulaElement.ElementType.USER_LIST, textViewSelection, null);
			}
		} else if (textViewSelectionGroup.equals(context.getString(R.string.simple_brick_group_number))) {
			ifCondition = new FormulaElement(FormulaElement.ElementType.NUMBER, textViewSelection, null);
		}

		if (isFirst) {
			setFormulaWithBrickField(BrickField.IF_SIMPLE_FIRST_CONDITION, new Formula(ifCondition));
		} else {
			setFormulaWithBrickField(BrickField.IF_SIMPLE_SECOND_CONDITION, new Formula(ifCondition));
		}
	}

	private void updateFormula() {
		if (!SimpleBrickUtil.isInFormulaEditor) {
			getFormulaWithBrickField(BrickField.IF_SIMPLE_FIRST_CONDITION).setTextFieldId(R.id
					.brick_repeat_begin_first_part_text_view);
			getFormulaWithBrickField(BrickField.IF_SIMPLE_FIRST_CONDITION).refreshTextField(view);

			getFormulaWithBrickField(BrickField.IF_SIMPLE_SECOND_CONDITION).setTextFieldId(R.id.brick_repeat_begin_second_part_text_view);
			getFormulaWithBrickField(BrickField.IF_SIMPLE_SECOND_CONDITION).refreshTextField(view);

			FormulaElement firstCondition = getFormulaWithBrickField(BrickField.IF_SIMPLE_FIRST_CONDITION).getRoot();
			FormulaElement secondCondition = getFormulaWithBrickField(BrickField.IF_SIMPLE_SECOND_CONDITION).getRoot();
			FormulaElement operatorCondition = getFormulaWithBrickField(BrickField.IF_SIMPLE_OPERATOR_CONDITION).getRoot();

			FormulaElement newFormula = new FormulaElement(FormulaElement.ElementType.OPERATOR, operatorCondition.getValue(), null);
			newFormula.setLeftChild(firstCondition);
			newFormula.setRightChild(secondCondition);

			setFormulaWithBrickField(BrickField.REPEAT_UNTIL_CONDITION, new Formula(newFormula));
		}
	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = View.inflate(context, R.layout.brick_repeat_until_simple, null);

		TextView textIfFirst = (TextView) prototypeView.findViewById(R.id.brick_repeat_begin_first_part_text_view);
		textIfFirst.setText(context.getString(R.string.formula_editor_sensor_loudness));

		TextView textIfSecond = (TextView) prototypeView.findViewById(R.id.brick_repeat_begin_second_part_text_view);
		textIfSecond.setText(String.valueOf(BrickValues.IF_SIMPLE_SECOND_CONDITION));

		Spinner spinnerOperator = (Spinner) prototypeView.findViewById(R.id.brick_repeat_begin_operator_spinner);
		ArrayAdapter<String> spinnerAdapterOperator = createArrayAdapterOperator(context);
		spinnerOperator.setAdapter(spinnerAdapterOperator);
		spinnerOperator.setSelection(BrickValues.IF_SIMPLE_OPERATOR_CONDITION);

		return prototypeView;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		SequenceAction ifAction = (SequenceAction) sprite.getActionFactory().createSequence();
		SequenceAction elseAction = (SequenceAction) sprite.getActionFactory().createSequence();
		Action action = sprite.getActionFactory().createIfLogicAction(sprite,
				getFormulaWithBrickField(BrickField.REPEAT_UNTIL_CONDITION), ifAction, elseAction);
		sequence.addAction(action);

		LinkedList<SequenceAction> returnActionList = new LinkedList<>();
		returnActionList.add(elseAction);
		returnActionList.add(ifAction);

		return returnActionList;
	}

	public void showFormulaEditorToEditFormula(View view) {
		switch (view.getId()) {
			case R.id.brick_repeat_begin_first_part_text_view:
				FormulaEditorFragment.showSimpleBrickFragment(view, this, BrickField.IF_SIMPLE_FIRST_CONDITION);
				break;

			case R.id.brick_repeat_begin_second_part_text_view:
				FormulaEditorFragment.showSimpleBrickFragment(view, this, BrickField.IF_SIMPLE_SECOND_CONDITION);
				break;
		}
	}

	@Override
	public Brick clone() {
		return new RepeatUntilSimpleBrick(getFormulaWithBrickField(BrickField.REPEAT_UNTIL_CONDITION).clone(),
				getFormulaWithBrickField(BrickField.IF_SIMPLE_FIRST_CONDITION).clone(), getFormulaWithBrickField(BrickField.IF_SIMPLE_SECOND_CONDITION).clone(), getFormulaWithBrickField(BrickField
				.IF_SIMPLE_OPERATOR_CONDITION).clone());
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		RepeatUntilSimpleBrick copyBrick = (RepeatUntilSimpleBrick) clone();
		copy = copyBrick;
		return copyBrick;
	}

	@Override
	public long getBeginLoopTime() {
		return beginLoopTime;
	}

	@Override
	public void setBeginLoopTime(long beginLoopTime) {
		this.beginLoopTime = beginLoopTime;
	}

	@Override
	public LoopEndBrick getLoopEndBrick() {
		return loopEndBrick;
	}

	@Override
	public void setLoopEndBrick(LoopEndBrick loopEndBrick) {
		this.loopEndBrick = loopEndBrick;
	}

	@Override
	public LoopBeginBrick getCopy() {
		return copy;
	}

	@Override
	public boolean isInitialized() {
		return (loopEndBrick != null);
	}

	@Override
	public void initialize() {
		loopEndBrick = new LoopEndBrick(this);
	}

	@Override
	public boolean isDraggableOver(Brick brick) {
		return (loopEndBrick != null);
	}

	@Override
	public List<NestingBrick> getAllNestingBrickParts(boolean sorted) {
		List<NestingBrick> nestingBrickList = new ArrayList<NestingBrick>();
		nestingBrickList.add(this);
		nestingBrickList.add(loopEndBrick);

		return nestingBrickList;
	}

	@Override
	public void updateReferenceAfterMerge(Scene into, Scene from) {
	}
}